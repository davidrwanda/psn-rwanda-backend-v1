# Setting Up Automatic Token Capture in Postman

This guide explains how to set up automatic token capturing from the login response in Postman for the PSN Rwanda API.

## Adding Test Scripts to Login Request

1. In the Postman collection, navigate to the **Authentication** folder
2. Select the **Login** request
3. Go to the **Tests** tab
4. Add the following script:

```javascript
// Check if login was successful
pm.test("Login successful", function() {
    pm.response.to.have.status(200);
    pm.response.to.be.json;
});

// Extract and save tokens from response
pm.test("Save auth tokens", function() {
    var jsonData = pm.response.json();
    
    // Check if response contains the token properties
    if (jsonData.accessToken) {
        pm.environment.set("authToken", jsonData.accessToken);
        console.log("Auth token saved to environment");
    }
    
    if (jsonData.refreshToken) {
        pm.environment.set("refreshToken", jsonData.refreshToken);
        console.log("Refresh token saved to environment");
    }
});
```

## How It Works

When you execute the Login request:

1. If the login is successful (returns a 200 status code)
2. The test script automatically extracts the `accessToken` and `refreshToken` from the response
3. The tokens are saved to the corresponding environment variables

## Testing the Setup

1. Send the **Login** request with valid credentials
2. Check the environment variables by clicking on the eye icon next to your environment in the top-right corner
3. You should see that the `authToken` and `refreshToken` variables are now populated

## Adding Test Scripts to Refresh Token Request

To update tokens when using the refresh endpoint, add this test script to the **Refresh Token** request:

```javascript
pm.test("Refresh token successful", function() {
    pm.response.to.have.status(200);
    pm.response.to.be.json;
});

pm.test("Save new auth tokens", function() {
    var jsonData = pm.response.json();
    
    if (jsonData.accessToken) {
        pm.environment.set("authToken", jsonData.accessToken);
        console.log("New auth token saved to environment");
    }
    
    if (jsonData.refreshToken) {
        pm.environment.set("refreshToken", jsonData.refreshToken);
        console.log("New refresh token saved to environment");
    }
});
```

## Common Issues

- **Token Not Being Captured**: Make sure the token field names in the script match the actual response from your API
- **Authorization Not Working**: Check that the Authorization header is properly set to use the environment variable
- **Token Expiration**: Use the Refresh Token endpoint to get a new access token when it expires

---

This setup will help you work efficiently with the PSN Rwanda API by automatically managing your authentication tokens. 