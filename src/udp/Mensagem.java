package udp;

import com.google.gson.Gson;

import java.io.Serializable;

public class Mensagem implements Serializable {

    private int numSeq;

    private byte[] data = new byte[1024];

    public Mensagem(int numSeq, byte[] data) {
        this.numSeq = numSeq;
        this.data = data;
    }

    public Mensagem() {

    }

    public int getNumSeq() {
        return numSeq;
    }

    public void setNumSeq(int numSeq) {
        this.numSeq = numSeq;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String mensagemToString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Mensagem stringToMensagem(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Mensagem.class);
    }

}
