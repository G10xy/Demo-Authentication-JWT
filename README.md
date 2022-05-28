# Demo-Authentication-JWT
Demo project showing backend jwt-based authentication

There are 3 roles: Admin, Iam, Editor.
A user with admin role is eligible to act on other admins and iams, a user with
iam role is eligible to act on other iams and editors, a user with editor role can not 
act on any user. 
For acting is meant user creation, suspension, deletion.
Editor user can by the way change its own password. 