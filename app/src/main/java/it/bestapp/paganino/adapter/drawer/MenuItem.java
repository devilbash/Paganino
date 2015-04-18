package it.bestapp.paganino.adapter.drawer;

/**
 * Created by Utente on 28/01/2015.
 */
public class MenuItem {
        private String nome;
        private int tipo;
        public MenuItem(int tipo, String name) {
            this.tipo = tipo;
            this.nome = name;
        }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNome() {
            return nome;
        }
    public int getTipo() {
        return tipo;
    }
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
