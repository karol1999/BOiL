public class Data {

    private int[] dostawcy = {20, 30, 65}; // dwóch dostawców, popyt
    private int[] odbiorcy = {10, 28, 27, 50}; // trzech odbiorców, podaż
    private int[][] kosztyTransportu = {{8,14,17}, {12,9,19}}; // jednostkowe koszty transportu
                                                                   // O1D1, O2D1, O3D1, O1D2, O2D2, O3D2
    private int[] cenySprzedazy = {30, 25, 30}; // ceny sprzedazy
    private int[] kosztyZakupu = {10, 12}; // koszty zakupu
    private int fikcyjnyOdbiorca = 50; // suma popytu
    private int fikcyjnyDostawca = 65; // suma podaży

    public int getFikcyjnyOdbiorca() {
        return fikcyjnyOdbiorca;
    }

    public void setFikcyjnyOdbiorca(int fikcyjnyOdbiorca) {
        this.fikcyjnyOdbiorca = fikcyjnyOdbiorca;
    }

    public int getFikcyjnyDostawca() {
        return fikcyjnyDostawca;
    }

    public void setFikcyjnyDostawca(int fikcyjnyDostawca) {
        this.fikcyjnyDostawca = fikcyjnyDostawca;
    }

    public int[] getDostawcy() {
        return dostawcy;
    }

    // SETTER DOSTAWCÓW
    public void setDostawcy(int index, int value) {
        dostawcy[index] = value;
    }

    public int[] getOdbiorcy() {
        return odbiorcy;
    }

    // SETTER ODBIORCÓW
    public void setOdbiorcy(int index, int value) {
        odbiorcy[index] = value;
    }

    public int[][] getKosztyTransportu() {
        return kosztyTransportu;
    }

    public void setKosztyTransportu(int[][] kosztyTransportu) {
        this.kosztyTransportu = kosztyTransportu;
    }

    public int[] getCenySprzedazy() {
        return cenySprzedazy;
    }

    public void setCenySprzedazy(int[] cenySprzedazy) {
        this.cenySprzedazy = cenySprzedazy;
    }

    public int[] getKosztyZakupu() {
        return kosztyZakupu;
    }

    public void setKosztyZakupu(int[] kosztyZakupu) {
        this.kosztyZakupu = kosztyZakupu;
    }
}
