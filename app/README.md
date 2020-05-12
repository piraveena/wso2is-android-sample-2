# Run this Android Sample App 


## Register Application

1. Register an application with the following callback-url.

    | Field                 | Value         | 
    | --------------------- | ------------- | 
    | Service Provider Name | sample-app  |
    | Description           | This is a mobile application  | 
    | Call Back Url         | com.example.myapplication://oauth  | 

2. Enable following properties:
- PKCE Mandatory
- Allow authentication without the client secret

3. Get the cliet-id

## Configure the Android SDK

### Build the SDK locally

1. Clone this project: https://github.com/piraveena/android-sdk.git

2. Build the library in your local maven. Run the following commands. Now the library will be available in your local .m2 cache. 
    - `./gradlew clean`
    - `./gradlew assembleRelease`
    - `./gradlew publishToMavenLocal `

##Configure the project

#### Configuration


Add the relevant configs. 
    - Add the client-id of the application.
    - Update the {HOST_NAME}:{PORT} with the IS server's hostname and port respectively

```json
{
 "client_id": {client-id},
 "redirect_uri": "com.example.myapplication://oauth",
 "authorization_scope": "openid",
 "authorization_endpoint": "https://{HOST_NAME}:{PORT}/oauth2/authorize",
 "end_session_endpoint": "https://{HOST_NAME}:{PORT}/oidc/logout",
 "token_endpoint": "https://{HOST_NAME}:{PORT}/oauth2/token"
}
```

Example:
```json
{
 "client_id": "tkJfn9a8Yw2kfRfUSIrfvemcVjYa",
 "redirect_uri": "com.example.myapplication://oauth",
 "authorization_scope": "openid",
 "authorization_endpoint": "https://10.0.2.2:9443/oauth2/authorize",
 "end_session_endpoint": "https://10.0.2.2:9443/oidc/logout",
 "token_endpoint": "https://10.0.2.2:9443/oauth2/token",
 "userinfo_endpoint": "https://10.0.2.2:9443/oauth2/userinfo"
}
```

## Run your application