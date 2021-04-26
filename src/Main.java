public class Main {
    public static void main(String[] args) {

        Logic logic = new Logic();

        // obliczenie jednostkowego zysku
        //logic.obliczJednostkowyZysk(); TO DOBRZE DZIALA
        logic.obliczPierwszePrzyblizenie(logic.obliczJednostkowyZysk());
        logic.wspolczynnikiAlfaBeta(logic.obliczJednostkowyZysk());
        logic.calculateArrayDelta();
        System.out.println("Witaj");
    }
}
