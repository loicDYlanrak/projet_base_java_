package base;

public class Ensemble {
    private Object[] elements;
    private int taille;

    public Ensemble() {
        this.elements = new Object[10];
        this.taille = 0;
    }

    public Ensemble(Object[] liObjects) {
        this.elements = liObjects;
        this.taille = liObjects.length;
    }

    public void ajouter(Object element) {
        //System.out.println(" ajouter element: " + element);
        if (taille == elements.length) {
            agrandirTableau();
        }
        if (!appartientEnsemble(element)) {
            elements[taille++] = element;
        }
    }

    public boolean appartient(Object element) {
        //System.out.println("element.getClass() :"+element.getClass());
        //System.out.println();
        for (int i = 0; i < elements.length; i++) {
            //System.out.println("elements["+i+"].getClass() :"+elements[i].getClass());
            //System.out.println();
            if (elements[i] != null) {
                if (elements[i].getClass().equals(Class.class)) {
                    // System.out.println(" class le izy");
                    if (elements[i].equals(element.getClass())) {
                        // System.out.println(" return true");
                        return true;
                    }
                } else {
                    if (elements[i].equals(element)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean appartientEnsemble(Object element) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != null) {
                if (elements[i].equals(element)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int cardinalite() {
        return taille;
    }


    public Ensemble union(Ensemble autre) throws Exception {
        Ensemble result = new Ensemble();
        for (int i = 0; i < this.elements.length; i++) {
            if (this.elements[i]!=null) {
                result.ajouter(this.elements[i]);    
            }
            
        }
        for (int i = 0; i < autre.elements.length; i++) {
            if (autre.elements[i]!=null) {
                result.ajouter(autre.elements[i]);    
            }
        }

        //System.out.println(" NEw ensemble ");
        for (int i = 0; i < result.getElements().length; i++) {
            //System.out.println("result.getElements()[i]: " + result.getElements()[i]);
        }

        return result;
    }

    public Ensemble intersection(Ensemble autre) {
        Ensemble result = new Ensemble();
        for (int i = 0; i < this.taille; i++) {
            if (autre.appartient(this.elements[i])) {
                result.ajouter(this.elements[i]);
            }
        }
        return result;
    }

    public Ensemble difference(Ensemble autre) {
        Ensemble result = new Ensemble();
        for (int i = 0; i < this.taille; i++) {
            if (!autre.appartient(this.elements[i])) {
                result.ajouter(this.elements[i]);
            }
        }
        return result;
    }

    private void agrandirTableau() {
        Object[] nouveauTableau = new Object[elements.length * 2];
        for (int i = 0; i < elements.length; i++) {
            nouveauTableau[i] = elements[i];
        }
        elements = nouveauTableau;
    }

    public boolean egal(Ensemble autre) {
        // Verifier la cardinalite (nombre d'elements)
        if (this.taille != autre.taille) {
            //System.out.println("taille differen, sa taille: "+taille+"  , taille de lautre "+autre.getTaille());
            return false;
        }
    
        // Verifier que chaque element de l'ensemble courant est dans l'autre ensemble
        for (int i = 0; i < this.taille; i++) {
            if (!autre.appartientEnsemble(this.elements[i])) {
                return false;
            }
        }
    
        // Verifier que chaque element de l'autre ensemble est dans l'ensemble courant
        for (int i = 0; i < autre.taille; i++) {
            if (!this.appartientEnsemble(autre.elements[i])) {
                return false;
            }
        }
    
        return true; // Les deux ensembles sont egaux
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{ ");
        for (int i = 0; i < taille; i++) {
            sb.append(elements[i]).append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

    public Object[] getElements() {
        return elements;
    }

    public int getTaille() {
        return taille;
    }

    public void setElements(Object[] elements) {
        this.elements = elements;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

}
