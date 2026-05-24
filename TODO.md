# TODO - Google OAuth2 Internal Server Error Fix

## Info gathered
- Frontend triggers Google OAuth: `${API_BASE_URL}/oauth2/authorization/google?redirect_uri=${encodeURIComponent(redirectUri)}`
- Backend uses Spring Security OAuth2 login + `OAuth2AuthenticationSuccessHandler` to redirect back with JWT tokens.
- Reported runtime error: `{"success":false,"message":"Internal Server Error"...}`.
- `OAuth2AuthenticationSuccessHandler` currently redirects when `redirect_uri` query param exists, else returns JSON.

## Suspected root cause
- The success handler may throw an exception during redirect URL/token creation, and that exception bubbles up as generic 500.
- Failure handler already redirects when `redirect_uri` query param exists, but success handler doesn’t guard against exceptions.

## Plan (implementation steps)
1. Update `OAuth2AuthenticationSuccessHandler` to wrap token generation + redirect building in try/catch.
   - On error: if `redirect_uri` exists, redirect to it with `error=oauth2_success_failed` + message.
   - Otherwise: return a consistent JSON `ApiResponse` with 401/400 (not 500).
2. Update `OAuth2AuthenticationFailureHandler` to ensure JSON includes the same `success:false` + message and never throws.
3. Rebuild backend.
4. Smoke test: trigger the Google OAuth flow and verify redirect back to `/login` with `token`/`refresh_token` OR `error=`.

## Followup steps
- Restart backend if needed and try the Google button again.
- If it still shows Internal Server Error, check backend logs for `OAuth2 success handler failed` and share the stacktrace.


