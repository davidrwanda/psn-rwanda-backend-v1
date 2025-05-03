# Email Deliverability Troubleshooting Guide

If your emails are being sent successfully (authentication succeeds) but are not being received, this guide will help you troubleshoot and resolve deliverability issues.

## 1. Check Spam/Junk Folders

**First step:** Always check the spam or junk folder in your email client.

**Why this happens:** Even when email sending is successful, receiving mail servers might flag your messages as spam due to:
- Sending domain reputation
- Missing or incorrect SPF, DKIM, or DMARC records
- Content that triggers spam filters
- New sending IP/domain

**Solution:**
- Add the sending email address to your contacts list
- Mark any found emails as "Not Spam"
- Check with different email providers (Gmail, Outlook, Yahoo, etc.)

## 2. DNS Configuration for Better Deliverability

**Why this matters:** Proper DNS configuration significantly improves email deliverability.

**Add these DNS records for your sending domain (oneclic.vet):**

1. **SPF Record (TXT record):**
   ```
   v=spf1 include:spf.infomaniak.ch ~all
   ```

2. **DKIM Records:** 
   - Access your Infomaniak management panel
   - Navigate to the DKIM settings for your domain
   - Enable DKIM signing for your emails
   - Verify DKIM keys are correctly published

3. **DMARC Record (TXT record named _dmarc):**
   ```
   v=DMARC1; p=none; rua=mailto:dmarc@oneclic.vet
   ```

## 3. Testing Email Delivery

There are several ways to test your email delivery:

1. **Basic Email Test:**
   ```
   ./test-emails.sh your.email@example.com
   ```

2. **Test With Attachment:**
   Use the attachment test which can help bypass spam filters:
   ```
   curl http://localhost:8080/api/v1/test/email/test-with-attachment?to=your.email@example.com
   ```

3. **View Mail Configuration:**
   ```
   curl http://localhost:8080/api/v1/test/email/configuration
   ```

## 4. Email Content Improvements

**Spam trigger avoidance:**
- Avoid spam-triggering words in subject lines 
- Always include plain text version alongside HTML
- Keep HTML simple and well-formed
- Include unsubscribe links even in transactional emails
- Add company physical address in footer

## 5. Advanced Testing Options

**Use external email testing services:**
1. **Mail-Tester:** https://www.mail-tester.com/
   - Sends you a unique email address
   - Send your test email to it
   - Analyzes your email for deliverability issues

2. **Check blacklists:** 
   - Use https://mxtoolbox.com/blacklists.aspx 
   - Enter your sending IP address or domain (oneclic.vet)

## 6. Common Gmail-Specific Issues

Gmail is particularly strict with email filtering:

1. **Volume ramp-up:**
   - Start with low volume and gradually increase
   - Build sending reputation gradually

2. **Authentication alignment:**
   - Ensure "From" address domain matches SPF/DKIM domain
   - Use consistent sending addresses

3. **Gmail Postmaster Tools:**
   - Register your domain at https://postmaster.google.com/
   - Monitor your domain reputation

## Temporary Workarounds

While resolving deliverability issues:

1. **Alternative Email Services:**
   If Infomaniak mail delivery is problematic, consider using a transactional email service:
   - SendGrid
   - Mailgun
   - Amazon SES

2. **Add clear instructions** to users to check spam folders
3. **Include a phone number** as an alternative contact method
4. **Try sending from a different email address** on the same domain

## Testing Next Steps

1. Test with the script using different recipient addresses:
   ```
   ./test-emails.sh your.email@example.com
   ```
2. Check all recipient spam folders thoroughly
3. Try sending to a different email provider
4. Test with the email + attachment option which often has better deliverability 