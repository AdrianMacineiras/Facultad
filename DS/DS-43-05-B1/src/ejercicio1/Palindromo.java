package ejercicio1;

public  class  Palindromo {
        public static boolean esPalindromo (String texto){
        String texto_min = texto.toLowerCase();
        int cabeza = 0;
        int cola = texto_min.length()-1;
        while (cola > cabeza){
            if (!esCaracterValido(texto_min.charAt(cabeza))){
                cabeza++;
                continue;
            }
            while (!esCaracterValido(texto_min.charAt(cola))){//while en lugar de if + continue, para evitar 
                cola--;                                    //comprobaciones innecesarias en el if anterior 
                if (cola==cabeza) break;
            }
            if (texto_min.charAt(cabeza)==texto_min.charAt(cola)){
                cabeza++;
                cola--;
            }
            else return false;
        }
        return true;
    }
     
    private static boolean esCaracterValido (char j){
        if ((j<97) || (j>122)) return false;
        else return true;
    }
}