package inmotion.sistema.uninassau.pe.model;


import android.graphics.Bitmap;

/**
 * Created by mirandagab on 07/07/2018.
 */
public class Anuncio {
    private String anunciante,
            telefone,
            webSite,
            appURL;
    private Bitmap imagem;
    private boolean anuncioAtivo;

    public Anuncio(String anunciante, String telefone, String webSite, String appURL,
                   Bitmap imagem, boolean anuncioAtivo) {
        this.anunciante = anunciante;
        this.telefone = telefone;
        this.webSite = webSite;
        this.appURL = appURL;
        this.imagem = imagem;
        this.anuncioAtivo = anuncioAtivo;
    }

    public String getAnunciante() {
        return anunciante;
    }

    public void setAnunciante(String anunciante) {
        this.anunciante = anunciante;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getAppURL() {
        return appURL;
    }

    public void setAppURL(String appURL) {
        this.appURL = appURL;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    public boolean isAnuncioAtivo() {
        return anuncioAtivo;
    }

    public void setAnuncioAtivo(boolean anuncioAtivo) {
        this.anuncioAtivo = anuncioAtivo;
    }
}
