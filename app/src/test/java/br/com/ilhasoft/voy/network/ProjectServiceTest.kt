package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.network.projects.ProjectApi
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * Created by jones on 2/27/18.
 */
class ProjectServiceTest {

    lateinit var projectService: MockProjectService

    val mockProjectId = 2

    @Before
    fun setup() {
        projectService = MockProjectService()
    }

    @Test
    fun shouldReturnProjects() {
        projectService.getProjects()
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { projects: List<Project> -> projects.isNotEmpty() }
    }

    @Test
    fun shouldNotReturnProjects() {
        projectService.getProjects()
                .test()
                .assertSubscribed()
                .assertError(HttpException::class.java)
    }

    //when the app runs for the first time and after login the user has no internet connection
    @Test
    fun shouldNotReturnProjectsOffline() {
        projectService.getProjects()
                .test()
                .assertSubscribed()
                .assertError(UnknownHostException::class.java)
    }

    @Test
    fun shouldReturnProjectById() {
        projectService.getProject(mockProjectId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { it.id == mockProjectId }
    }

}

class MockProjectService : ServiceFactory<ProjectApi>(ProjectApi::class.java) {

    val mockName = "A Fake Project"
    val mockProjectList = mutableListOf(
            Project(1, "Fake Project"),
            Project(2, "Fake Project 2"))

    fun getProjects(): Flowable<MutableList<Project>> =
            Flowable.just(mockProjectList)


    fun getProject(projectId: Int): Single<Project> =
            Single.just(Project(projectId, mockName))

}