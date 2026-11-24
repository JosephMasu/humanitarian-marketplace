package org.humanitarian.marketplace.user.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

class KeycloakRealmRoleConverterTests {

    private final KeycloakRealmRoleConverter converter = new KeycloakRealmRoleConverter();

    @Test
    void mapsRealmRolesToPrefixedAuthorities() {
        Jwt jwt = jwt(Map.of("realm_access", Map.of("roles", List.of("ngo-staff", "donor"))));

        assertThat(converter.convert(jwt))
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_ngo-staff", "ROLE_donor");
    }

    @Test
    void returnsNoAuthoritiesWhenRealmAccessIsMissing() {
        assertThat(converter.convert(jwt(Map.of("scope", "openid")))).isEmpty();
    }

    private static Jwt jwt(Map<String, Object> claims) {
        Jwt.Builder builder = Jwt.withTokenValue("token").header("alg", "none").subject("user-1");
        claims.forEach(builder::claim);
        return builder.build();
    }
}
