public class Word {
    private String wordEN;
    private String meanTR;

    public String getWordEN() {
        return wordEN;
    }


    public void setWordEN(String wordEN) {
        this.wordEN = wordEN;
    }

    public String getMeanTR() {
        return meanTR;
    }

    public void setMeanTR(String meanTR) {
        this.meanTR = meanTR;
    }

    public Word(String wordEN, String meanTR) {
        this.wordEN = wordEN;
        this.meanTR = meanTR;
    }
}
