package br.com.ilhasoft.voy.project

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
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
class ProjectDataTest {

    @Mock
    lateinit var projectRemoteDataSource: ProjectDataSource
    @Mock
    lateinit var projectLocalDataSource: ProjectDataSource
    @Mock
    lateinit var connectionProvider: CheckConnectionProvider

    lateinit var projectRepository: ProjectRepository

    val mockProjectId = 2
    val mockedLanguage = "en"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        projectRepository = ProjectRepository(projectRemoteDataSource, projectLocalDataSource, connectionProvider)
    }

    @Test
    fun shouldReturnProjects() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(projectRemoteDataSource.getProjects(mockedLanguage))
                .thenReturn(Flowable.just(createMockProjectList()))

        projectRepository.getProjects(mockedLanguage)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { projects: List<Project> -> projects.isNotEmpty() }
    }

    @Test
    fun shouldNotReturnProjects() {
        `when`(projectRemoteDataSource.getProjects(mockedLanguage))
                .thenReturn(Flowable.error(TimeoutException()))

        projectRemoteDataSource.getProjects(mockedLanguage)
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun shouldNotReturnProjectsUnknownHost() {
        `when`(connectionProvider.hasConnection()).thenReturn(true)
        `when`(projectRemoteDataSource.getProjects(mockedLanguage)).thenReturn(Flowable.error(UnknownHostException()))

        projectRepository.getProjects(mockedLanguage)
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    @Test
    fun shouldReturnSingleProject() {
        `when`(projectRemoteDataSource.getProject(mockProjectId, mockedLanguage))
                .thenReturn(Single.just(createMockProject()))

        projectRepository.getProject(mockProjectId, mockedLanguage)
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