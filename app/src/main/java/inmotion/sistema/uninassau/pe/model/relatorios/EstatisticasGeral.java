package inmotion.sistema.uninassau.pe.model.relatorios;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class EstatisticasGeral implements Serializable {

    private Timestamp dataInicial, dataFinal; //Já criei get's que retornam em String formatada
    int QtdViagens, QtdServicos; //Contar as quantidades para todos veiculo
    float totalDistancia, totalGastos; //Somar os valores para todos veiculo
    HashMap<String, Float> totalDistanciaPorCategoria, totalGastosPorCategoria; //Totais por categoria (mesmo acima, só que dividido nas 4 categorias)
    //Ex:
    //[{Particular,150.56},
    // {Público,24.00},
    // {Compartilhado,150.00},
    // {Alugado,100.00}]
    HashMap<String, Float> proporcaoDistanciaPorCategoria, proporcaoGastosPorCategoria; // A soma das 4 proporções vai sempre ser 100% em cada caso.  Considerar em valores, não em quantidades esta proporção.

    private static SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");

    public EstatisticasGeral() {
    }

    public Timestamp getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Timestamp dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Timestamp getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Timestamp dataFinal) {
        this.dataFinal = dataFinal;
    }

    public int getQtdViagens() {
        return QtdViagens;
    }

    public void setQtdViagens(int qtdViagens) {
        QtdViagens = qtdViagens;
    }

    public int getQtdServicos() {
        return QtdServicos;
    }

    public void setQtdServicos(int qtdServicos) {
        QtdServicos = qtdServicos;
    }

    public float getTotalDistancia() {

        if (Float.isInfinite(totalDistancia)) {
            return 100;
        } else if (Float.isNaN(totalDistancia)) {
            return 0;
        } else {
            return totalDistancia;
        }

    }

    public void setTotalDistancia(float totalDistancia) {
        this.totalDistancia = totalDistancia;
    }

    public float getTotalGastos() {
        if (Float.isInfinite(totalGastos)) {
            return 100;
        } else if (Float.isNaN(totalGastos)) {
            return 0;
        } else {
            return totalGastos;
        }
    }

    public void setTotalGastos(float totalGastos) {
        this.totalGastos = totalGastos;
    }

    public HashMap<String, Float> getTotalDistanciaPorCategoria() {
        return totalDistanciaPorCategoria;
    }

    public void setTotalDistanciaPorCategoria(HashMap<String, Float> totalDistanciaPorCategoria) {
        this.totalDistanciaPorCategoria = totalDistanciaPorCategoria;
    }

    public HashMap<String, Float> getTotalGastosPorCategoria() {
        return totalGastosPorCategoria;
    }

    public void setTotalGastosPorCategoria(HashMap<String, Float> totalGastosPorCategoria) {
        this.totalGastosPorCategoria = totalGastosPorCategoria;
    }

    public HashMap<String, Float> getProporcaoDistanciaPorCategoria() {
        return proporcaoDistanciaPorCategoria;
    }

    public void setProporcaoDistanciaPorCategoria(HashMap<String, Float> proporcaoDistanciaPorCategoria) {
        this.proporcaoDistanciaPorCategoria = proporcaoDistanciaPorCategoria;
    }

    public HashMap<String, Float> getProporcaoGastosPorCategoria() {
        return proporcaoGastosPorCategoria;
    }

    public void setProporcaoGastosPorCategoria(HashMap<String, Float> proporcaoGastosPorCategoria) {
        this.proporcaoGastosPorCategoria = proporcaoGastosPorCategoria;
    }

    public String getDataInicialTxt() {
        return out.format(new Date(this.dataInicial.getTime()));
    }

    public String getDataFinalTxt() {
        return out.format(new Date(this.dataFinal.getTime()));
    }

    public Set getTotalDistanciaPorCategoriaSet() {
        return totalDistanciaPorCategoria.entrySet();
    }

    public Set gettotalGastosPorCategoriaSet() {
        return totalGastosPorCategoria.entrySet();
    }

    public Set getproporcaoDistanciaPorCategoriaSet() {
        return proporcaoDistanciaPorCategoria.entrySet();
    }

    public Set getproporcaoGastosPorCategoriaSet() {
        return proporcaoGastosPorCategoria.entrySet();
    }
}
