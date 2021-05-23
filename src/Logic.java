import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class Logic { // logika biznesowa calego programu

    private static final int ILOSC_DOSTAWCOW = 2;
    private static final int ILOSC_ODBIORCOW = 3;

    // tablice ogólne potrzebne do poprawnego działania programu
    private int[][] resultTablicaJednostkowyZysk; // tablica z wartościami jednostkowego zysku
    private int[][] resultTablicaPierwszePrzyblizenieMaxMatrix; // tablica potrzebna do wyznaczenia pierwszego przyblizenia
                                                                // metoda maksymalnego elementu macierzy
    private int[] wspAlfa;
    private int[] wspBeta;

    private int[][] deltaArray;
    private boolean [][] isInCycle; // if true means that it is in cycle otherwise false
    // private int[][] coordinates;


    // warunek stopu funkcji (obliczPierwszePrzybliżenie), pierwsze rozwiązanie bazowe
    private boolean stopCondition(int[][] array){

        for(int row=0;row<array.length;row++){
            for (int column=0;column<array[row].length;column++){
                if(array[row][column] == Integer.MAX_VALUE){
                    return true; // cos jeszcze jest do zrobienia
                }
                else {
                    continue;
                }
            }
        }
        return false; // juz nic nie mamy do zrobienia
    }

    // zwraca tablice z wartościami jednostkowego zysku
    public int[][] obliczJednostkowyZysk(){ // z = c - kz - kt -> c to jest cena sprzedzy
                                         // kz to jest koszt zakupu
                                         // kt to jest koszt transportu

        Data data = new Data();

        // []->rows[]->columns
        resultTablicaJednostkowyZysk = new int[3][4]; // 3 rzędy oraz 4 kolumny

        for(int row = 0; row < 2; row++){
            for(int column = 0; column < 3; column++){
                resultTablicaJednostkowyZysk[row][column] = (data.getCenySprzedazy())[column]-(data.getKosztyZakupu())[row]-(data.getKosztyTransportu())[row][column];
            }
        }

        // fikcyjny odbiorca, fikcyjny dostawca
        resultTablicaJednostkowyZysk[0][3] = 0;
        resultTablicaJednostkowyZysk[1][3] = 0;
        resultTablicaJednostkowyZysk[2][3] = 0;
        resultTablicaJednostkowyZysk[2][0] = 0;
        resultTablicaJednostkowyZysk[2][1] = 0;
        resultTablicaJednostkowyZysk[2][2] = 0;

        return resultTablicaJednostkowyZysk;
    }


    // -----------------------------------------------------------------------


    // wyznaczenie pierwszego przyblizenia (pierwsze rozwiązanie bazowe)
    public int[][] obliczPierwszePrzyblizenie(int[][] tablicaJednostkowyZysk){

        Data data = new Data(); // ładowanie danych
        resultTablicaPierwszePrzyblizenieMaxMatrix = new int[3][4]; // tablica, która będzie miała wartości pierwszego przybliżenia
        int[][] maxElementCoordinates = new int[1][2]; // wspołrzedne najwiekszego elementu
        int[][] tablicaZWspolrzednymiJuzZapisanymi = new int[3][4]; // potrzebne bedzie pozniej do wyznaczania wspolrzednych max elementu

        // tablica ze wspolrzednymi juz zapisanymi
        for(int row = 0; row < tablicaZWspolrzednymiJuzZapisanymi.length; row++) {
            for (int column = 0; column < tablicaZWspolrzednymiJuzZapisanymi[row].length; column++) {
                tablicaZWspolrzednymiJuzZapisanymi[row][column] = Integer.MAX_VALUE; // zapełniamy maksymalnymi wartosciami
                // jezeli wartosc jest maksymalnie dodatnia to oznacza, że wartosc nie jest zajeta
            }
        }

        // zapełniamy zerami tablice
        for(int row = 0; row < resultTablicaPierwszePrzyblizenieMaxMatrix.length; row++) {
            for (int column = 0; column < resultTablicaPierwszePrzyblizenieMaxMatrix[row].length; column++) {
                resultTablicaPierwszePrzyblizenieMaxMatrix[row][column] = Integer.MAX_VALUE; // zapełniamy bardzo duzymi wartosciami
            }
        }

        // uzupełniamy całą tablice wartościami bazowymi
        for(int row = 0; row < resultTablicaPierwszePrzyblizenieMaxMatrix.length; row++){
            for(int column = 0; column < resultTablicaPierwszePrzyblizenieMaxMatrix[row].length; column++){

                if(stopCondition(tablicaZWspolrzednymiJuzZapisanymi) == false){
                    return resultTablicaPierwszePrzyblizenieMaxMatrix;
                }

                // maxElementCoordinates[0][0] -> rząd, maxElementCoordinates[0][1] -> kolumna
                maxElementCoordinates = findMaxCoordinates(tablicaJednostkowyZysk, tablicaZWspolrzednymiJuzZapisanymi); // najpierw szukamy najwiekszego elementu (jego współrzędnych)

                // jezeli odbiorca jest wiekszy od dostawcy to zapisujemy dostawce, odejmujemy odbiorce od dostawcy oraz ustawiamy X na rzedzie tam gdzie jest dostawca
                if((data.getOdbiorcy())[maxElementCoordinates[0][1]] > (data.getDostawcy())[maxElementCoordinates[0][0]]){
                    // ustawianie wartosci dostawcy
                    resultTablicaPierwszePrzyblizenieMaxMatrix[maxElementCoordinates[0][0]][maxElementCoordinates[0][1]] = (data.getDostawcy())[maxElementCoordinates[0][0]];

                    // odejmowanie odbiorcy od dostawcy TO TRZEBA POPRAWIC
                    data.setOdbiorcy(maxElementCoordinates[0][1], (data.getOdbiorcy())[maxElementCoordinates[0][1]] - (data.getDostawcy())[maxElementCoordinates[0][0]]);
                    data.setDostawcy(maxElementCoordinates[0][0], 0);

                    // ustawianie X na rzedzie, TO TEZ TRZEBA POPRAWIC
                    for(int column1=0;column1<3;column1++){
                        if(column1 == maxElementCoordinates[0][1])
                        {
                            continue;
                        }
                        else if(resultTablicaPierwszePrzyblizenieMaxMatrix[maxElementCoordinates[0][0]][column1] != Integer.MAX_VALUE){
                            continue;
                        }
                        resultTablicaPierwszePrzyblizenieMaxMatrix[maxElementCoordinates[0][0]][column1] = Integer.MIN_VALUE;
                    }

                    // zapelniamy to co juz zajete
                    tablicaZWspolrzednymiJuzZapisanymi[maxElementCoordinates[0][0]][maxElementCoordinates[0][1]] = Integer.MIN_VALUE;

                    // lecimy caly rzad czyli kolumnami
                    tablicaZWspolrzednymiJuzZapisanymi[maxElementCoordinates[0][0]][0] = Integer.MIN_VALUE;
                    tablicaZWspolrzednymiJuzZapisanymi[maxElementCoordinates[0][0]][1] = Integer.MIN_VALUE;
                    tablicaZWspolrzednymiJuzZapisanymi[maxElementCoordinates[0][0]][2] = Integer.MIN_VALUE;
                    tablicaZWspolrzednymiJuzZapisanymi[maxElementCoordinates[0][0]][3] = Integer.MIN_VALUE;


                } // jezeli dostawca jest wiekszy od odbiorcy to zapisujemy odbiorce, odejmujemy dostawce od odbiorcy oraz ustawiamy X na kolumnie tam gdzie jest odbiorca
                else{
                    // ustawianie wartosci odbiorcy
                    resultTablicaPierwszePrzyblizenieMaxMatrix[maxElementCoordinates[0][0]][maxElementCoordinates[0][1]] = (data.getOdbiorcy())[maxElementCoordinates[0][1]];

                    // odejmowanie dostawcy od odbiorcy TO TRZEBA POPRAWIC
                    data.setDostawcy(maxElementCoordinates[0][0], (data.getDostawcy())[maxElementCoordinates[0][0]] - (data.getOdbiorcy())[maxElementCoordinates[0][1]]);
                    data.setOdbiorcy(maxElementCoordinates[0][1], 0);


                    // ustawianie X na kolumnie, TO TEŻ TRZEBA POPRAWIĆ
                    for(int row1=0;row1<3;row1++){
                        if(row1 == maxElementCoordinates[0][0])
                        {
                            continue;
                        }
                        else if(resultTablicaPierwszePrzyblizenieMaxMatrix[row1][maxElementCoordinates[0][1]] != Integer.MAX_VALUE)
                        {
                            continue;
                        }
                        resultTablicaPierwszePrzyblizenieMaxMatrix[row1][maxElementCoordinates[0][1]] = Integer.MIN_VALUE;
                    }

                    // zapisujemy to co juz zajete
                    tablicaZWspolrzednymiJuzZapisanymi[maxElementCoordinates[0][0]][maxElementCoordinates[0][1]] = Integer.MIN_VALUE;

                    // lecimy całą jedną kolumną czyli rzedami
                    tablicaZWspolrzednymiJuzZapisanymi[0][maxElementCoordinates[0][1]] = Integer.MIN_VALUE;
                    tablicaZWspolrzednymiJuzZapisanymi[1][maxElementCoordinates[0][1]] = Integer.MIN_VALUE;
                    tablicaZWspolrzednymiJuzZapisanymi[2][maxElementCoordinates[0][1]] = Integer.MIN_VALUE;
                }
            }
        }

        System.out.println();
        return resultTablicaPierwszePrzyblizenieMaxMatrix;
    }

    // funkcja, która zwraca współrzędne maksymalnego elementu
    // tablicaJednostkowyZysk -> zielone wartości, pierwszeRozwiązanieBazoweMaxMatrix -> niebieskie wartości
    public int[][] findMaxCoordinates(int[][] tablicaJednostkowyZysk, int[][] zajeteWspolrzedne){

        int[][] maxElementCoordinates = new int[1][2]; // 1 rząd 2 kolumny

        int maxValue = Integer.MIN_VALUE;

        // jezeli zostana same 0 to wtedy odpalamy inny funkcje (kolejny warunek)
        //...

        // lecimy przez cała tablice
        for(int row = 0; row < tablicaJednostkowyZysk.length; row++){
            for (int column = 0; column < tablicaJednostkowyZysk[row].length; column++){

                // musi tu byc warunek aby nie brac juz obliczonych wartosci i X (jako X dac bardzo dużą wartość ujemną)
                if(zajeteWspolrzedne[row][column] == Integer.MIN_VALUE){ // zapełnione X
                    continue;
                }
                else{ // jezeli jest wszystko w porzadku
                    if(tablicaJednostkowyZysk[row][column] > maxValue && maxValue < 0 && (row == 2 || column == 3)){ // jezeli maksymalny element to wartosc ujemna przy wartosciach 0
                        continue;
                    } // tutaj z wartoscia ujemna
                    else if(tablicaJednostkowyZysk[row][column] > maxValue){ // jeżeli znajdziemy nowy maksymalny element
                        maxValue = tablicaJednostkowyZysk[row][column];
                        maxElementCoordinates[0][0] = row;
                        maxElementCoordinates[0][1] = column;
                    }
                    else{ // jeżeli nie znajdziemy nowego maksymalnego elementu
                        continue;
                    }
                }

            }
        }

        if(maxValue == Integer.MIN_VALUE){
            for(int row = 0; row < tablicaJednostkowyZysk.length; row++){
                for (int column = 0; column < tablicaJednostkowyZysk[row].length; column++) {
                    if(zajeteWspolrzedne[row][column] == Integer.MIN_VALUE){ // zapełnione X
                        continue;
                    }
                    else{
                        if(tablicaJednostkowyZysk[row][column] > maxValue){ // jeżeli znajdziemy nowy maksymalny element
                            maxValue = tablicaJednostkowyZysk[row][column];
                            maxElementCoordinates[0][0] = row;
                            maxElementCoordinates[0][1] = column;
                        }
                        else{ // jeżeli nie znajdziemy nowego maksymalnego elementu
                            continue;
                        }
                    }
                }
            }
        }

        System.out.println();
        return maxElementCoordinates;
    }

    // ---------------------------------------------

    public void wspolczynnikiAlfaBeta(int[][] resultTablicaJednostkowyZysk){

        // 0 -> 1
        // 1 -> 2
        // 2 -> 3
        int[] wspoczynnikiAlfaTablica = new int[3];
        int[] wspoczynnikiBetaTablica = new int[4];

        for(int i=0;i<3;i++){
            wspoczynnikiAlfaTablica[i] = Integer.MAX_VALUE;
        }

        for(int j=0;j<4;j++){
            wspoczynnikiBetaTablica[j] = Integer.MAX_VALUE;
        }

        wspoczynnikiAlfaTablica[0] = 0;

        for(int k = 0; k < 3 * 4;++k) {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 4; ++j) {
                    if (resultTablicaPierwszePrzyblizenieMaxMatrix[i][j] > Integer.MIN_VALUE) {
                        if (wspoczynnikiBetaTablica[j] == Integer.MAX_VALUE && wspoczynnikiAlfaTablica[i] != Integer.MAX_VALUE)
                            wspoczynnikiBetaTablica[j] = resultTablicaJednostkowyZysk[i][j] - wspoczynnikiAlfaTablica[i];
                        else if (wspoczynnikiBetaTablica[j] != Integer.MAX_VALUE && wspoczynnikiAlfaTablica[i] == Integer.MAX_VALUE)
                            wspoczynnikiAlfaTablica[i] = resultTablicaJednostkowyZysk[i][j] - wspoczynnikiBetaTablica[j];
                    }
                }
            }
        }

        wspAlfa = wspoczynnikiAlfaTablica;
        wspBeta = wspoczynnikiBetaTablica;
    }

    // policzenie tablicy delta
    public void calculateArrayDelta(){
        deltaArray = new int[3][4];

        // obsadzenie wartosc ekstremalnie minimalnych w tablicy delta
        for(int row=0;row<resultTablicaPierwszePrzyblizenieMaxMatrix.length;row++){
            for(int column=0;column<resultTablicaPierwszePrzyblizenieMaxMatrix[row].length;column++){
                if(resultTablicaPierwszePrzyblizenieMaxMatrix[row][column] != Integer.MIN_VALUE){
                    deltaArray[row][column] = Integer.MIN_VALUE;
                }
                else{
                    continue;
                }
            }
        }

        for(int row=0;row<resultTablicaPierwszePrzyblizenieMaxMatrix.length;row++){
            for(int column=0;column<resultTablicaPierwszePrzyblizenieMaxMatrix[row].length;column++){
                if(deltaArray[row][column] != Integer.MIN_VALUE){
                    deltaArray[row][column] = resultTablicaJednostkowyZysk[row][column]-wspAlfa[row]-wspBeta[column];
                }
                else {
                    continue;
                }
            }
        }
    }

    // znajdujemy cykl
    public void findCycle()
    {
        // 1, 2, 3 ,4
//        int[][] coordinates = new int[2][4];
//
//        int maxValue = deltaArray[0][0];
//        // znajdujemy maksymalny element w tablicy delta
//        for(int row=0;row<deltaArray.length;row++){
//            for(int column=0;column<deltaArray[row].length;column++){
//                if(deltaArray[row][column] > maxValue){
//                    maxValue = deltaArray[row][column];
//                    coordinates[0][0] = row;
//                    coordinates[1][0] = column;
//                }
//                else {
//                    continue;
//                }
//            }
//        }
//
//        // konczymy poniewaz mamy rozwiazanie optymalne
//        if(maxValue < 0){
//            return;
//        }
//
//        // sprawdzamy sąsiadów, mamy zawsze 4 mozliwosci
//        int row = deltaArray.length;
//        int column = deltaArray[row].length;
//
//        // sprawdzamy gore
//        if(deltaArray[coordinates[0][0]-1][coordinates[0][1]] == Integer.MIN_VALUE && (coordinates[0][0]-1) < deltaArray.length){
//
//        }

        // ------------------------------------------ nowa wersja ---------------------------------------------------- //

        isInCycle = new boolean[3][4]; // rozmiar taki jak delta array

//        deltaArray[0][0] = Integer.MIN_VALUE;
//        deltaArray[0][1] = -3;
//        deltaArray[0][2] = Integer.MIN_VALUE;
//        deltaArray[0][3] = -10;
//
//        deltaArray[1][0] = Integer.MIN_VALUE;
//        deltaArray[1][1] = -4;
//        deltaArray[1][2] = 1;
//        deltaArray[1][3] = -9;
//
//        deltaArray[2][0] = -6;
//        deltaArray[2][1] = -5;
//        deltaArray[2][2] = -7;
//        deltaArray[2][3] = -8;

        int secondNodeX = 0; // x-współrzędna pierwszego noda i potem czwartego
        int fourthNodeY = 0; // y-wspolrzedna pierwszego noda i potem czwartego
        int thirdNodeY = 0;
        // int thirdNodeX = 0;

        int maxValue = Integer.MIN_VALUE;
        // znajdujemy maksymalny element w tablicy delta
        for(int row=0;row<deltaArray.length;row++){
            for(int column=0;column<deltaArray[row].length;column++){
                if(deltaArray[row][column] > maxValue){
                    maxValue = deltaArray[row][column];
                    secondNodeX = row;
                    fourthNodeY = column;
                }
                else {
                    continue;
                }
            }
        }

        // konczymy poniewaz mamy rozwiazanie optymalne
        if(maxValue < 0){
            return;
        }

        isInCycle[secondNodeX][fourthNodeY] = true;

        // szukanie drugiego węzła
        for(int column=0;column<deltaArray[0].length;column++){ // dlatego 0 bo kazdy wiersza ma ta sama dlugosc, lecimy po kolumnach
            if(column==fourthNodeY || deltaArray[secondNodeX][column]!=Integer.MIN_VALUE){
                continue;
            }
            else if(deltaArray[secondNodeX][column]==Integer.MIN_VALUE){
                //boolean thirdNodeFound = existsThirdNode(column); // dostaniemy informacje czy trzeci wezel istnieje w poszukiwanej kolumnie
                if(existsThirdNode(column, secondNodeX)==false){
                    continue;
                }
                else{ // if true
                    isInCycle[secondNodeX][column] = true;
                    thirdNodeY = column; //
                }
            }
        }

        // szukanie trzeciego węzła
        for(int row=0;row<deltaArray.length;row++){
            if(row==secondNodeX || deltaArray[row][thirdNodeY]!=Integer.MIN_VALUE){ // jezeli
                continue;
            }else if(deltaArray[row][thirdNodeY]==Integer.MIN_VALUE){
                if(existsFourthNode(row, fourthNodeY)){
                    isInCycle[row][fourthNodeY] = true; // ustawienie czwartego wezla
                    isInCycle[row][thirdNodeY] = true; // ustawienie trzeciego węzła
                }
            }
        }
        System.out.println();
    }

    private boolean existsThirdNode(int actualColumn, int potentialSecondNodeX){
        for(int row=0;row<deltaArray.length;row++){
            if(row==potentialSecondNodeX){
                continue;
            }

            if(deltaArray[row][actualColumn]==Integer.MIN_VALUE){
                return true;
            }else{
                continue;
            }
        }

        return false;
    }

    private boolean existsFourthNode(int actualRow, int fourthNodeColumn){
        if(deltaArray[actualRow][fourthNodeColumn]==Integer.MIN_VALUE){
            return true;
        }

        return false;
    }

}
