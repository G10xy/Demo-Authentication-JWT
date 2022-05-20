package it.giovi.controller

import it.giovi.model.request.auth.SignUpRequest
import it.giovi.model.request.user.UserChangePasswordRequest
import it.giovi.model.request.user.UserInfoChangeRequest
import it.giovi.model.request.user.UserLostPasswordRequest
import it.giovi.model.response.SuccessResponse
import it.giovi.model.response.UserResponse
import it.giovi.persistence.entity.UserRoleEntity.UserRoleEnum
import it.giovi.persistence.entity.UserStateEntity.UserStateEnum
import it.giovi.security.JwtUserDetailsImpl
import it.giovi.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/management/users")
class UserControllerImpl(
    private val userService: UserService
) {

    @GetMapping(name = "/{id}")
    fun getUser(@PathVariable("id") id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(userService.findUser(id))
    }

    /*The postfilter filters returned list of users containing only users whose role is the same or one step below of logged user*/
    @GetMapping("/advancedFiltering")
    @PreAuthorize("{hasAnyRole('ADMIN', 'IAM')}")
    @PostFilter("hasRole('IAM') ? filterObject.getUserRole().name() != 'ROLE_EDITOR' : filterObject.getUserRole().name() != 'ROLE_ADMIN'")
    fun getUsersByFiltering(
        @RequestParam(value = "username", required = false, defaultValue = "") username: String,
        @RequestParam(value = "name", required = false, defaultValue = "") name: String,
        @RequestParam(value = "surname", required = false, defaultValue = "") surname: String,
        @RequestParam(value = "role", required = false) role: UserRoleEnum,
        @RequestParam(value = "state", required = false) state: UserStateEnum
    ): Iterable<UserResponse> {
        return userService.findAllByFiltering(username, name, surname, role, state)
    }


    @GetMapping("/roles")
    @PreAuthorize("{hasAnyRole('ADMIN', 'IAM')}")
    fun getRoles(): Iterable<String> {
        return userService.findAllRoles()
    }

    @GetMapping("/states")
    @PreAuthorize("{hasAnyRole('ADMIN', 'IAM')}")
    fun getStates(): Iterable<String> {
        return userService.findAllStates()
    }

    @PostMapping("/signUp")
    @PreAuthorize(
        "{(hasRole('ADMIN') and (#request.role.toString() == 'ROLE_ADMIN' or #request.role.toString() == 'ROLE_IAM')) " +
                "or (hasRole('IAM') and (#request.role.toString() == 'ROLE_IAM' or #request.role.toString() == 'ROLE_EDITOR'))}")
    fun registerUser(
        @Valid @RequestBody request: SignUpRequest, authentication: Authentication): ResponseEntity<SuccessResponse> {
        userService.createUser(request, authentication)
        return ResponseEntity(SuccessResponse("User correctly logged in"), HttpStatus.CREATED)
    }

    @PutMapping("/password")
    fun modifyPassword(
        @RequestBody dtoRequest: @Valid UserChangePasswordRequest, authentication: Authentication): ResponseEntity<SuccessResponse> {
        val jwtUser = authentication.principal as JwtUserDetailsImpl
        jwtUser.id?.let { userService.modifyPassword(it, dtoRequest) }
        return ResponseEntity.ok(SuccessResponse("Password successfully updated"))
    }

    @PutMapping(name = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'IAM') and @userSecurityServiceImpl.isNotOnItself(#id, #authentication)")
    fun modifyUser(@PathVariable("id") id: Long, @RequestBody dtoRequest: @Valid UserInfoChangeRequest, authentication: Authentication): ResponseEntity<SuccessResponse> {
        userService.modifyUser((id), (dtoRequest), (authentication))
        return ResponseEntity.ok(SuccessResponse("User successfully modified"))
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasAnyRole('ADMIN', 'IAM') and @userSecurityServiceImpl.isNotOnItself(#id, #authentication)")
    fun disableUser(@PathVariable("id") id: Long, authentication: Authentication): ResponseEntity<SuccessResponse> {
        userService.suspendUser(id, authentication)
        return ResponseEntity.ok(SuccessResponse("User correctly suspended"))
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasAnyRole('ADMIN', 'IAM') and @userSecurityServiceImpl.isNotOnItself(#id, #authentication)")
    fun enableUser(@PathVariable("id") id: Long, authentication: Authentication): ResponseEntity<SuccessResponse> {
        userService.enableUser(id, authentication)
        return ResponseEntity.ok(SuccessResponse("User correctly enabled"))
    }

    @PutMapping("/lostPassword")
    fun lostPassword(@RequestBody dtoRequest: @Valid UserLostPasswordRequest, authentication: Authentication): ResponseEntity<SuccessResponse> {
        val jwtUser = authentication.principal as JwtUserDetailsImpl
        jwtUser.id?.let { userService.lostPassword(it, dtoRequest, authentication) }
        return ResponseEntity.ok(SuccessResponse("Reset password successfully done"))
    }

    @PutMapping("/{id}/resetPassword")
    @PreAuthorize("hasAnyRole('ADMIN', 'IAM') and @userSecurityServiceImpl.isNotOnItself(#id, #authentication)")
    fun resetUserPassword(@PathVariable("id") id: Long, authentication: Authentication): ResponseEntity<SuccessResponse> {
        userService.resetPassword(id, authentication)
        return ResponseEntity.ok(SuccessResponse("Reset password successfully done"))
    }

    @DeleteMapping(name = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'IAM') and @userSecurityServiceImpl.isNotOnItself(#id, #authentication)")
    fun deleteUser(@PathVariable("id") id: Long, authentication: Authentication): ResponseEntity<SuccessResponse> {
        userService.deleteUser(id)
        return ResponseEntity.ok(SuccessResponse("User successfully removed"))
    }

    @PostMapping("/signOut")
    fun userSignOut(request: HttpServletRequest, authentication: Authentication): ResponseEntity<SuccessResponse> {
        userService.registerLastSignOut(authentication)
        return ResponseEntity.ok(SuccessResponse("Logout success"))
    }


}