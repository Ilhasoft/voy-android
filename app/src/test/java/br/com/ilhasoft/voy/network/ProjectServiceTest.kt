package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.network.projects.ProjectDataSource
import br.com.ilhasoft.voy.network.projects.ProjectRepository
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Created by jones on 2/27/18.
 */
class ProjectServiceTest {

    @Mock
    lateinit var projectService: ProjectDataSource

    lateinit var projectRepository: ProjectRepository

    val mockProjectId = 2

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        projectRepository = ProjectRepository(projectService)
    }

    @Test
    fun shouldReturnProjects() {
        `when`(projectService.getProjects())
                .thenReturn(Flowable.just(createMockProjectList()))


        projectRepository.getProjects()
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { projects: List<Project> -> projects.isNotEmpty() }
    }

    @Test
    fun shouldNotReturnProjects() {
        `when`(projectService.getProjects())
                .thenReturn(Flowable.error(TimeoutException()))

        projectService.getProjects()
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun shouldNotReturnProjectsUnknownHost() {
        `when`(projectService.getProjects()).thenReturn(Flowable.error(UnknownHostException()))

        projectRepository.getProjects()
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    @Test
    fun shouldReturnSingleProject() {
        `when`(projectService.getProject(mockProjectId))
                .thenReturn(Single.just(createMockProject()))

        projectRepository.getProject(mockProjectId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { project -> project.id == mockProjectId }
    }

    private fun createMockProject(): Project =
            Project(mockProjectId, "FakeProject")

    private fun createMockProjectList(): MutableList<Project> {
        return mutableListOf(
                Project(1, "FakeProject 1"),
                Project(2, "FakeProject 2"),
                Project(3, "FakeProject 3"),
                Project(4, "FakeProject 4")
        )
    }
}