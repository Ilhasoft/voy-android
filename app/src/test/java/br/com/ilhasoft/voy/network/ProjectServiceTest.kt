package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.network.authorization.AuthorizationResponse
import br.com.ilhasoft.voy.network.authorization.AuthorizationService
import br.com.ilhasoft.voy.network.projects.ProjectService
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

/**
 * Created by jones on 2/27/18.
 */
class ProjectServiceTest {

    private lateinit var authService: AuthorizationService
    private lateinit var projectService: ProjectService
    private val credentials: Credentials = Credentials("pirralho", "123456")
    private val mockProjectId = 2

    @Before
    fun setup() {
        authService = AuthorizationService()
        getToken(credentials).subscribe()
        projectService = ProjectService()
    }

    @Test
    fun shouldReturnUserProjects() {
        projectService.getProjects()
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { projects: List<Project> -> projects.isNotEmpty() }
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

    private fun getToken(credentials: Credentials): Flowable<AuthorizationResponse> =
            authService.loginWithCredentials(credentials)
                    .doOnNext { BaseFactory.accessToken = it.token }

}