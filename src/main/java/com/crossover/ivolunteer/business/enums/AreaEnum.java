//package com.crossover.ivolunteer.business.enums;
//
//import java.util.Objects;
//
//public enum AreaEnum {
//    ANIMAIS("Animais"),
//    CRIANCAS("Crianças"),
//    CULTURA_E_ARTE("Cultura e Arte"),
//    DIREITOS_HUMANOS("Direitos Humanos"),
//    EDUCACAO("Educação"),
//    ESPORTES("Esportes"),
//    IDOSOS("Idosos"),
//    JOVENS("Jovens"),
//    LGBTQ("LGBTQ+"),
//    MEIO_AMBIENTE("Meio Ambiente"),
//    MULHERES("Mulheres"),
//    PESSOAS_COM_DEFICIENCIA("Pessoas com Deficiência"),
//    POLITICA("Política"),
//    REFUGIADOS("Refugiados"),
//    SAUDE("Saúde"),
//    OUTRAS("Outras");
//
//    private final String texto;
//
//    AreaEnum(String texto) {
//        this.texto = texto;
//    }
//
//    public static AreaEnum fromTexto(String texto) {
//        AreaEnum[] values = AreaEnum.values();
//        for (AreaEnum value : values) {
//            if (Objects.equals(texto, value.getTexto())) {
//                return value;
//            }
//        }
//        throw new IllegalArgumentException();
//    }
//
//    public String getTexto() {
//        return texto;
//    }
//}
