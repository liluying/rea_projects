#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
export ANDROID_HOME="${ANDROID_HOME:-/workspace/.tools/android-sdk}"
export ANDROID_USER_HOME="${ANDROID_USER_HOME:-/workspace/.cache/android}"
export GRADLE_USER_HOME="${GRADLE_USER_HOME:-/workspace/.cache/gradle}"
if [[ -x /workspace/.tools/jdk21/bin/jlink && -z "${JAVA_HOME:-}" ]]; then
  export JAVA_HOME=/workspace/.tools/jdk21
fi
mkdir -p "$ANDROID_USER_HOME" "$GRADLE_USER_HOME" /workspace/.cache/android-home
# The cloud user's home is read-only. Keep Java/Android caches inside the workspace.
export JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS:-} -Duser.home=/workspace/.cache/android-home"
if [[ -n "${HTTPS_PROXY:-}" ]]; then
  read -r proxy_host proxy_port < <(python3 - <<'PY'
import os,urllib.parse
p=urllib.parse.urlparse(os.environ['HTTPS_PROXY'])
print(p.hostname or '',p.port or 8080)
PY
)
  export JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -Dhttps.proxyHost=$proxy_host -Dhttps.proxyPort=$proxy_port -Dhttp.proxyHost=$proxy_host -Dhttp.proxyPort=$proxy_port"
fi
if [[ -f /workspace/.cache/java/cacerts ]]; then
  export JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -Djavax.net.ssl.trustStore=/workspace/.cache/java/cacerts"
fi
if [[ ! -f "$ANDROID_HOME/platforms/android-35/android.jar" ]]; then
  echo 'Android SDK 35 is missing. Install platforms;android-35 and build-tools;35.0.0 with Android Studio or sdkmanager.' >&2
  exit 1
fi
exec ./gradlew "${@:-:app:assembleDebug}" --console=plain
