# Email Authentication Troubleshooting Guide

If you're experiencing "Authentication failed" errors when sending emails, here are some common causes and solutions:

## 1. Incorrect Password

The most common reason for authentication failure is an incorrect password.

**Solution:**
- Verify the password is correct by logging into webmail at mail.privateemail.com
- Ensure there are no leading/trailing spaces in the password
- If you use special characters in your password, make sure they're properly escaped in application.properties

## 2. Username Format Issues

Some SMTP servers are sensitive to how the username is formatted.

**Solution:**
- Try using just the username part without the domain: `noreply` instead of `noreply@umusada.com`
- Alternatively, try the full email address if you're currently using just the username

## 3. Two-Factor Authentication (2FA)

If your email account has 2FA enabled, regular password authentication may fail.

**Solution:**
- Generate an app-specific password from your email provider
- Use this app-specific password in your application.properties instead of your regular password

## 4. Security Settings on Email Provider

Some email providers block login attempts from unknown applications.

**Solution:**
- Check your email provider's security settings
- Look for options to allow "less secure apps" or "third-party applications"
- Check if login attempts are being blocked (check your email for security alerts)

## 5. Network/Firewall Issues

Your network or firewall might be blocking the connection to the mail server.

**Solution:**
- Try running the application from a different network
- Check if port 465 is open on your network for outbound connections
- Verify your hosting provider allows outbound SMTP connections

## 6. SSL/TLS Configuration

SSL/TLS configuration might be incorrect.

**Solution:**
- Try different SSL/TLS settings:
  ```properties
  # Option 1: SSL
  spring.mail.port=465
  spring.mail.properties.mail.smtp.ssl.enable=true
  spring.mail.properties.mail.smtp.starttls.enable=false
  
  # Option 2: TLS
  spring.mail.port=587
  spring.mail.properties.mail.smtp.ssl.enable=false
  spring.mail.properties.mail.smtp.starttls.enable=true
  ```

## Testing with External Tools

To verify if the issue is with your code or the mail server itself, try these external tools:

1. **Command line with telnet:**
   ```bash
   telnet mail.privateemail.com 465
   ```
   If you can't connect, it might be a network issue.

2. **Use a simple mail client** like Thunderbird or Apple Mail with the same credentials to see if they work.

3. **Try a simple Java mail test program** outside your Spring application to isolate if it's a Spring configuration issue or a general mail server issue.

## Debug Logs

When troubleshooting, look for these specific errors in your debug logs:

- `535 5.7.8 Authentication failed` - Password is incorrect
- `Connection refused` - Network/firewall issue
- `Unknown host` - DNS issue
- `Handshake failed` - SSL/TLS configuration issue

Remember to remove your email password from your application.properties file before committing code to version control! 