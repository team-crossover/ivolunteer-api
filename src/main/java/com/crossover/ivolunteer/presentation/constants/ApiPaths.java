package com.crossover.ivolunteer.presentation.constants;

public class ApiPaths {

    public static class V1 {

        public static final String PREFIX = "/api/v1";

        // ----------------------------------------------------
        // Apenas para admins
        // ----------------------------------------------------

        public static final String ADMIN_PREFIX = PREFIX + "/admin";

        // ----------------------------------------------------
        // Apenas para voluntários
        // ----------------------------------------------------

        public static final String VOLUNTARIO_PREFIX = PREFIX + "/voluntario";

        // ----------------------------------------------------
        // Apenas para ongs
        // ----------------------------------------------------

        public static final String ONG_PREFIX = PREFIX + "/ong";

        // ----------------------------------------------------
        // Disponível para todos os públicos
        // ----------------------------------------------------

        public static final String PUBLIC_PREFIX = PREFIX + "/public";

        // Autenticação
        private static final String AUTH_PREFIX = PUBLIC_PREFIX + "/auth";
        public static final String AUTH_AUTHENTICATE = AUTH_PREFIX + "/authenticate";
        public static final String AUTH_DEAUTHENTICATE = AUTH_PREFIX + "/deauthenticate";
        public static final String AUTH_WHOAMI = AUTH_PREFIX + "/whoami";

        // ONGs
        public static final String ONGS_PREFIX = PUBLIC_PREFIX + "/ongs";
        public static final String VOLUNTARIOS_PREFIX = PUBLIC_PREFIX + "/voluntarios";

    }
}
