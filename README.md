# OAuth2 password grant user registration portal
Basic OAuth2 password grant user registration and login portal.

Build the workspace:
## Compile:
```bash
foo@bar:~$ gradle wrapper
foo@bar:~$ ./gradlew clean
foo@bar:~$ ./gradlew build -x test
```

## Usage:
Run the web server (port 8080)
```bash
foo@bar:~$ ./gradlew bootRun
```

Then open http://localhost:8080/

## Sample data
```
username: example@example.com
password: password
```
Note to register an account you will need an active mail server to recieve the account activation email. I like to use [email-automation project](https://github.com/hughpearse/email-automation)
