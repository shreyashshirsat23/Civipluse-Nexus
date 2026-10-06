import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: import.meta.env.VITE_KEYCLOAK_URL || "http://localhost:8080",
  realm: import.meta.env.VITE_KEYCLOAK_REALM || "civicpulse",
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID || "civicpulse-web",
});

let initPromise = null;

export function initKeycloak() {
  if (!initPromise) {
    initPromise = keycloak.init({
      onLoad: "login-required",
      pkceMethod: "S256",
      checkLoginIframe: false,
      redirectUri: window.location.origin,
    });
  }

  return initPromise;
}

export default keycloak;