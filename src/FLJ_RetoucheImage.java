/* NFA032 CNAM Paris - Frédéric Le Joncour
 */
import java.io.*;
import java.util.ArrayList;
class Image{
  int largeur, hauteur;
  String nom;
  int[] tab;
  public Image(int l, int h, int[] t, String n) {
    this.largeur = l;
    this.hauteur = h;
    this.tab = t;
    this.nom = n;
  } 
  void reduction(){
    Integer[] tab2;
    ArrayList<Integer> tab3 = new ArrayList<>();
    for(int i=0; i<tab.length; i=i+(2*largeur)) {
      for(int j=i; j<(i+largeur); j=j+2) {
        tab3.add(tab[j]);
      }
    }
    tab2=new Integer[tab3.size()];  
    tab2=tab3.toArray(tab2);
    tab =new int[tab2.length];
    for(int i=0; i<tab2.length; i++) {
      tab[i]= tab2[i].intValue();
    } 
    largeur=(largeur/2)+1;
    hauteur=(hauteur/2)+1;
    new Afficheur(largeur, hauteur, tab, 800, 100, nom);
  } 
  void rotation() {
    int[] tab2 = new int[tab.length];
    int nbtouri = 1;
    int nbtourj = 0;
    for(int i=0; i<hauteur; i++) {
      for(int j=i; j<=((largeur*(hauteur-1))); j=j+hauteur) {    
        tab2[j]=tab[((hauteur-(nbtouri))*largeur)+nbtourj];
        nbtourj++;
      }
      nbtourj=0;
      nbtouri++;
    }
    new Afficheur(hauteur, largeur, tab2, 800, 100, nom);
  }
  int[] filtre(String coul) throws CouleurPasDefinie {
    if(coul.equals("Rouge") || coul.equals("rouge")) {
      for(int i=0; i<tab.length; i=i+2) {
        tab[i]=-65536;
      }
    }
    else if(coul.equals("Bleu") || coul.equals("bleu")) {
      for(int i=0; i<tab.length; i=i+2) {
        tab[i]=-16776961;
      }
    }
    else if(coul.equals("Vert") || coul.equals("vert")) {
      for(int i=0; i<tab.length; i=i+2) {
        tab[i]=-16711936;
      }
    }
    else if(coul.equals("Jaune") || coul.equals("jaune")) {
      for(int i=0; i<tab.length; i=i+2) {
        tab[i]=-256;
      }
    }
    else if(coul.equals("Magenta") || coul.equals("magenta")) {
      for(int i=0; i<tab.length; i=i+2) {
        tab[i]=-65281;
      }
    }
    else if(coul.equals("Cyan") || coul.equals("cyan")) {
      for(int i=0; i<tab.length; i=i+2) {
        tab[i]=-16711681;
      }
    }
    else {
      throw new CouleurPasDefinie();
    }
    return tab;
  }
  void rogner(int io, int jo, int l, int h) {
    int indiceor = (jo*largeur) + io; 
    int[] tab2 = new int[l*h];
    int indicetab2 = 0;
    for(int i=indiceor; i<(indiceor+(largeur*h)); i=(i+largeur)) {
      for(int j=i; j<(i+l); j++){
        tab2[indicetab2] = tab[j];
        indicetab2++;
      }
    }
    new Afficheur(l, h, tab2, 550, 100, nom);
  }
  int[] incruster(int io, int jo, Image im2) {
    int indiceor = (jo*largeur) + io; 
    int indicetab2 = 0;
    for(int i=indiceor; i<(indiceor+(largeur*im2.hauteur)); i=(i+largeur)) {
      for(int j=i; j<(i+im2.largeur); j++){
        tab[j] = im2.tab[indicetab2];
        indicetab2++;
      }
    }
    return tab;
  } 
}
abstract class Forme{
  int epaisseur, iorigine, jorigine, indiceorigine;
  String couleur;
  int couleurint = 0;
  Image imorigine;
  public Forme(int ep, int ior, int jor, String coul, Image im){
    this.epaisseur = ep;
    this.iorigine = ior;
    this.jorigine = jor;
    this.couleur = coul;
    this.imorigine = im;
    this.indiceorigine = (jor*im.largeur) + ior;
    if(couleur.equals("noir") || couleur.equals("Noir")) {
      couleurint = -16777216;
    }
    if(couleur.equals("blanc") || couleur.equals("Blanc")) {
      couleurint = 0;
    }
    if(couleur.equals("rouge") || couleur.equals("Rouge")) {
      couleurint = -65536;
    }
    if(couleur.equals("vert") || couleur.equals("Vert")) {
      couleurint = -16711936;
    }
    if(couleur.equals("bleu") || couleur.equals("Bleu")) {
      couleurint = -16776961;
    }
    if(couleur.equals("jaune") || couleur.equals("Jaune")) {
      couleurint = -256;
    }
    if(couleur.equals("magenta") || couleur.equals("Magenta")) {
      couleurint = -65281;
    }
    if(couleur.equals("cyan") || couleur.equals("Cyan")) {
      couleurint = -16711681;
    }
  }
  String getCouleur() {
    return this.couleur;
  }
  void setCouleur(String nvcouleur) {
    this.couleur = nvcouleur;
  }
  int getEpaisseur() {
    return this.epaisseur;
  }
  void setEpaisseur(int nvepaisseur) {
    this.epaisseur = nvepaisseur;
  }
  int getIOrigine() {
    return iorigine;
  }
  int getJOrigine() {
    return jorigine;
  }
  void setOrigine(int nviorigine, int nvjorigine){
    this.iorigine = nviorigine;
    this.jorigine = nvjorigine;
  } 
}
class Cercle extends Forme{
  int rayon;
  public Cercle(int ep, int ior, int jor, String coul, Image im, int r) {
    super(ep, ior, jor, coul, im);
    this.rayon = r;
  } 
  int[] tracerCercle() {
    int i=1;
    int j=1;
    for(int g=0; g<=((imorigine.hauteur-1)*(imorigine.largeur)); g=g+imorigine.largeur) {
      for(int f=g; f<(g+(imorigine.largeur)); f++) {
        if((Math.pow((iorigine-i), 2.0))+Math.pow((jorigine-j), 2.0)>=(Math.pow((rayon-(epaisseur/2)), 2.0)) && (Math.pow((iorigine-i), 2.0))+Math.pow((jorigine-j), 2.0)<=(Math.pow((rayon+(epaisseur/2)), 2.0))) {
          imorigine.tab[f] = couleurint;
        }
        i++;
      }
      j++;
      i=1;  
    }
    return imorigine.tab;
  } 
  int getRayon() {
    return this.rayon;
  }
  void setRayon(int r) {
    this.rayon = r;
  } 
}
class Trait extends Forme{
  int ifinal, jfinal;
  public Trait(int ep, int ior, int jor, String coul, Image im, int ifi, int jfi) {
    super(ep, ior, jor, coul, im);
    this.ifinal = ifi;
    this.jfinal = jfi;
  }
  int[] tracerTrait() {
    int i=0;
    int j=0;
    int indice1 = 0;
    int indice2 = 0;
    int indicefinal = (jfinal*imorigine.largeur) + ifinal;
    double coefdirecteur = 0.0;
    if(indiceorigine>indicefinal) {
      indice1=indicefinal;
      indice2=indiceorigine;
      if(jorigine==jfinal) {
        for(int g=indice1; g<(indice1+(imorigine.largeur*epaisseur)); g=(g+imorigine.largeur)) {
          for(int f=g; f<g+(indice2-indice1); f++){
            imorigine.tab[f] = couleurint;
          }
        }
      }
      if(iorigine==ifinal) {
        for(int g=indice1; g<((indice1+(jorigine-jfinal)*imorigine.largeur)); g=(g+401)) {
          for(int f=g; f<(g+epaisseur); f++) {
            imorigine.tab[f] = couleurint;
          }
        }
      }
      else {
        i=-iorigine;
        j=jorigine-jfinal;
        coefdirecteur = ((double)jfinal-(double)jorigine)/((double)ifinal-(double)iorigine);
        for(int g=indice1-ifinal; g<(indice2-imorigine.largeur); g=g+imorigine.largeur) {
          for(int f=g; f<(g+(imorigine.largeur)); f++) {
            if((j<=-coefdirecteur*i+((epaisseur/2)+1)) && (j>=-coefdirecteur*i-((epaisseur/2)-1))) {
              imorigine.tab[f] = couleurint;
            }
            i++;
          }
          j--;
          i=-iorigine;
        }
      }
    }
    else if(indicefinal>indiceorigine) {
      indice1=indiceorigine;
      indice2=indicefinal;
      if(jorigine==jfinal) {
        for(int g=indice1; g<(indice1+(imorigine.largeur*epaisseur)); g=(g+imorigine.largeur)) {
          for(int f=g; f<g+(indice2-indice1); f++){
            imorigine.tab[f] = couleurint;
          }
        }
      }
      if(iorigine==ifinal) {
        for(int g=indice1; g<((indice1+(jfinal-jorigine)*imorigine.largeur)); g=(g+401)) {
          for(int f=g; f<(g+epaisseur); f++) {
            imorigine.tab[f] = couleurint;
          }
        }
      }
      else {
        i=-ifinal;
        j=jfinal-jorigine;
        coefdirecteur = ((double)jfinal-(double)jorigine)/((double)ifinal-(double)iorigine);
        for(int g=indice1-iorigine; g<(indice2-imorigine.largeur); g=g+imorigine.largeur) {
          for(int f=g; f<(g+(imorigine.largeur)); f++) {
            if((j<=-coefdirecteur*i+((epaisseur/2)+1)) && (j>=-coefdirecteur*i-((epaisseur/2)-1))) {
              imorigine.tab[f] = couleurint;
            }
            i++;
          }
          j--;
          i=-ifinal;
        }
      }
    }
    return imorigine.tab;
  } 
  int getIFinal() {
    return ifinal;
  }
  int getJFinal() {
    return jfinal;
  }
  void setPointFinal(int nvif, int nvjf) {
    this.ifinal = nvif;
    this.jfinal = nvjf;
  }
  int getIInitial(){
    return iorigine;
  }
  int getJInitial() {
    return jorigine;
  }
  void setPointInitial(int nvio, int nvjo) {
    this.iorigine = nvio;
    this.jorigine = nvjo;
  }
}
class Rectangle extends Forme{
  int largeur, hauteur;
  public Rectangle(int ep, int ior, int jor, String coul, Image im, int l, int h) {
    super(ep, ior, jor, coul, im);
    this.largeur = l;
    this.hauteur = h;
  }
  int[] tracerRectangle() {
    for(int i=indiceorigine; i<(indiceorigine+(imorigine.largeur*epaisseur)); i=(i+imorigine.largeur)) {
      for(int j=i; j<(i+largeur); j++){
        imorigine.tab[j] = couleurint;
      }
    }
    for(int i=(indiceorigine+(imorigine.largeur*epaisseur)); i<((indiceorigine+(imorigine.largeur*epaisseur))+(hauteur*imorigine.largeur)); i=(i+401)) {
      for(int j=i; j<(i+epaisseur); j++) {
        imorigine.tab[j] = couleurint;
      }
    }
    for(int i=((indiceorigine+(imorigine.largeur*epaisseur))+largeur-epaisseur); i<((((indiceorigine+(imorigine.largeur*epaisseur))+largeur-epaisseur)+(hauteur*imorigine.largeur))); i=(i+401)) {
      for(int j=i; j<(i+epaisseur); j++) {
        imorigine.tab[j] = couleurint;
      }
    }
    for(int i=((indiceorigine+(imorigine.largeur*epaisseur))+(hauteur*imorigine.largeur)); i<((indiceorigine+(imorigine.largeur*epaisseur))+((hauteur*imorigine.largeur))+(imorigine.largeur*epaisseur)); i=(i+401)) {
      for(int j=i; j<(i+largeur); j++){
        imorigine.tab[j] = couleurint;
      }
    }
    return imorigine.tab;
  }
  int getHauteur() {
    return this.hauteur;
  }
  void setHauteur(int nvhauteur) {
    this.hauteur = nvhauteur;
  }
  int getLargeur() {
    return this.largeur;
  }
  void setLargeur(int nvlargeur) {
    this.largeur = nvlargeur;  
  }
}
class Triangle extends Forme{
  int ip2, jp2, ip3, jp3;
  public Triangle(int ep, int ior, int jor, String coul, Image im, int ip2, int jp2, int ip3, int jp3) {
    super(ep, ior, jor, coul, im);
    this.ip2 = ip2;
    this.jp2 = jp2;
    this.ip3 = ip3;
    this.jp3 = jp3;
  } 
  int[] tracerTriangle() {
    Trait t1 = new Trait(epaisseur, iorigine, jorigine, couleur, imorigine, ip2, jp2);
    Image i2 = new Image(imorigine.largeur, imorigine.hauteur, t1.tracerTrait(), "Image 2");
    Trait t2 = new Trait(epaisseur, ip2, jp2, couleur, i2, ip3, jp3);
    Image i3 = new Image(imorigine.largeur, imorigine.hauteur, t2.tracerTrait(), "Image 2");
    Trait t3 = new Trait(epaisseur, ip3, jp3, couleur, i3, iorigine, jorigine);
    return t3.tracerTrait();
  } 
  int getIP3() {
    return this.ip3;
  }
  int getJP3() {
    return this.jp3;
  }
  void setP3(int nvi, int nvj) {
    this.ip3 = nvi;
    this.jp3 = nvj;
  }
}
public class FLJ_RetoucheImage {
  public static void main(String[] args) throws IOException, EpaisseurTropGrande, IIncorrect, JIncorrect, RayonIncorrect, LargeurIncorrecte, HauteurIncorrecte {
    Image parthenon = new Image(ImageUtil.getImageWidth("parthenon.jpg"), ImageUtil.getImageHeight("parthenon.jpg"), ImageUtil.readImage("parthenon.jpg"), "Parthenon");
    Image chaton = new Image(ImageUtil.getImageWidth("chaton.jpg"), ImageUtil.getImageHeight("chaton.jpg"), ImageUtil.readImage("chaton.jpg"), "Chaton");
    Afficheur aff = new Afficheur(parthenon.largeur, parthenon.hauteur, parthenon.tab, 800, 100, parthenon.nom + " retouché");
    for(;;) {
      try {
        Terminal.ecrireStringln("1 : Ajout d'un trait		6 : Rotation à 90°");
        Terminal.ecrireStringln("2 : Ajout d'un cercle   	7 : Rogner");
        Terminal.ecrireStringln("3 : Ajout d'un rectangle  	8 : Filtrer");
        Terminal.ecrireStringln("4 : Ajout d'un triangle   	9 : Réduire");
        Terminal.ecrireStringln("5 : Incrustation   		0 : Sortie");
        int choix = Terminal.lireInt();
        if(choix==0) {
          Terminal.ecrireStringln("Bye bye");
          break;
        }
        else if(choix==1) {
          Terminal.ecrireString("Epaisseur (en pixels) ?");
          int ep = Terminal.lireInt();
          if(ep<0 || ep>parthenon.largeur) {
            throw new EpaisseurTropGrande();
          }
          Terminal.ecrireString("i initial (en pixels) ?");
          int i = Terminal.lireInt();
          if(i<0 || i>parthenon.largeur) {
            throw new IIncorrect();
          }
          Terminal.ecrireString("j initial (en pixels) ?");
          int j = Terminal.lireInt();
          if(j<0 || j>parthenon.hauteur) {
            throw new JIncorrect();
          }
          Terminal.ecrireString("i final (en pixels) ?");
          int ifi = Terminal.lireInt();
          if(ifi<0 || ifi>parthenon.largeur) {
            throw new IIncorrect();
          }
          Terminal.ecrireString("j final (en pixels) ?");
          int jfi = Terminal.lireInt();
          if(j<0 || j>parthenon.hauteur) {
            throw new JIncorrect();
          }
          Terminal.ecrireString("Couleur ?");
          String coul = Terminal.lireString();
          Trait t = new Trait(ep, i, j, coul, parthenon, ifi, jfi);
          aff.update(t.tracerTrait());
        }
        else if(choix==2) {
          Terminal.ecrireString("Epaisseur (en pixels) ?");
          int ep = Terminal.lireInt();
          if(ep<0 || ep>parthenon.largeur) {
            throw new EpaisseurTropGrande();
          }
          Terminal.ecrireString("i du centre (en pixels) ?");
          int i = Terminal.lireInt();
          if(i<0 || i>parthenon.largeur) {
            throw new IIncorrect();
          }
          Terminal.ecrireString("j du centre (en pixels) ?");
          int j = Terminal.lireInt();
          if(j<0 || j>parthenon.hauteur) {
            throw new JIncorrect();
          }
          Terminal.ecrireString("rayon (en pixels) ?");
          int ray = Terminal.lireInt();
          if(ray<0 || ray>(parthenon.largeur/2)) {
            throw new RayonIncorrect();
          }
          Terminal.ecrireString("Couleur ?");
          String coul = Terminal.lireString();
          Cercle c = new Cercle(ep, i, j, coul, parthenon, ray);
          aff.update(c.tracerCercle());
        }
        else if(choix==3) {
          Terminal.ecrireString("Epaisseur (en pixels) ?");
          int ep = Terminal.lireInt();
          if(ep<0 || ep>parthenon.largeur) {
            throw new EpaisseurTropGrande();
          }
          Terminal.ecrireString("i du coin haut gauche (en pixels) ?");
          int i = Terminal.lireInt();
          if(i<0 || i>parthenon.largeur) {
            throw new IIncorrect();
          }
          Terminal.ecrireString("j du coin haut gauche (en pixels) ?");
          int j = Terminal.lireInt();
          if(j<0 || j>parthenon.hauteur) {
            throw new JIncorrect();
          }
          Terminal.ecrireString("largeur (en pixels) ?");
          int larg = Terminal.lireInt();
          if(larg<0 || larg>parthenon.largeur) {
            throw new LargeurIncorrecte();
          }
          Terminal.ecrireString("hauteur (en pixels) ?");
          int haut = Terminal.lireInt();
          if(haut<0 || haut>parthenon.hauteur) {
            throw new HauteurIncorrecte();
          }
          Terminal.ecrireString("Couleur ?");
          String coul = Terminal.lireString();
          Rectangle r = new Rectangle(ep, i, j, coul, parthenon, larg, haut);
          aff.update(r.tracerRectangle());
        }
        else if(choix==4) {
          Terminal.ecrireString("Epaisseur (en pixels) ?");
          int ep = Terminal.lireInt();
          if(ep<0 || ep>parthenon.largeur) {
            throw new EpaisseurTropGrande();
          }
          Terminal.ecrireString("i du premier point (en pixels) ?");
          int i1 = Terminal.lireInt();
          if(i1<0 || i1>parthenon.largeur) {
            throw new IIncorrect();
          }
          Terminal.ecrireString("j du premier point (en pixels) ?");
          int j1 = Terminal.lireInt();
          if(j1<0 || j1>parthenon.hauteur) {
            throw new JIncorrect();
          }
          Terminal.ecrireString("i du second point (en pixels) ?");
          int i2 = Terminal.lireInt();
          if(i2<0 || i2>parthenon.largeur) {
            throw new IIncorrect();
          }
          Terminal.ecrireString("j du second point (en pixels) ?");
          int j2 = Terminal.lireInt();
          if(j2<0 || j2>parthenon.hauteur) {
            throw new JIncorrect();
          }
          Terminal.ecrireString("i du troisième point (en pixels) ?");
          int i3 = Terminal.lireInt();
          if(i3<0 || i3>parthenon.largeur) {
            throw new IIncorrect();
          }
          Terminal.ecrireString("j du troisième point (en pixels) ?");
          int j3 = Terminal.lireInt();
          if(j3<0 || j3>parthenon.hauteur) {
            throw new JIncorrect();
          }
          Terminal.ecrireString("Couleur ?");
          String coul = Terminal.lireString();
          Triangle tri = new Triangle(ep, i1, j1, coul, parthenon, i2, j2, i3, j3);
          aff.update(tri.tracerTriangle());
        }
        else if(choix==5) {
          Terminal.ecrireString("i du coin haut gauche (en pixels) ?");
          int i = Terminal.lireInt();
          if(i<0 || i>parthenon.largeur) {
            throw new IIncorrect();
          }
          Terminal.ecrireString("j du coin haut gauche (en pixels) ?");
          int j = Terminal.lireInt();
          if(j<0 || j>parthenon.hauteur) {
            throw new JIncorrect();
          }
          aff.update(parthenon.incruster(i, j, chaton));
        }
        else if(choix==6) {
          aff.masquer();
          parthenon.rotation();
        }
        else if(choix==7) {
          Terminal.ecrireString("i du coin haut gauche (en pixels) ?");
          int io = Terminal.lireInt();
          if(io<0 || io>parthenon.largeur) {
            throw new IIncorrect();
          }
          Terminal.ecrireString("j du coin haut gauche (en pixels) ?");
          int jo = Terminal.lireInt();
          if(jo<0 || jo>parthenon.hauteur) {
            throw new JIncorrect();
          }
          Terminal.ecrireString("largeur (en pixels) ?");
          int larg = Terminal.lireInt();
          if(larg<0 || larg>parthenon.largeur) {
            throw new LargeurIncorrecte();
          }
          Terminal.ecrireString("hauteur (en pixels) ?");
          int haut = Terminal.lireInt();
          if(haut<0 || haut>parthenon.hauteur) {
            throw new HauteurIncorrecte();
          }
          aff.masquer();
          parthenon.rogner(io, jo, larg, haut);
        }
        else if(choix==8) {
          try {
            Terminal.ecrireString("Couleur ?");
            String coul = Terminal.lireString();
            aff.update(parthenon.filtre(coul));
          } catch (CouleurPasDefinie c) {
            Terminal.ecrireStringln("Cette couleur n'existe pas.");
            Terminal.sautDeLigne();
          }
        }
        else if(choix==9) {
          aff.masquer();
          parthenon.reduction();
        }
        else if(choix<0 || choix>9) {
          Terminal.ecrireStringln("Veuillez taper un des chiffres propos�s.");
          Terminal.sautDeLigne();
        }
      } catch (EpaisseurTropGrande e) {
        Terminal.ecrireStringln("Epaisseur incorrecte !");
        Terminal.sautDeLigne();
      } catch (IIncorrect i) {
        Terminal.ecrireStringln("i incorrect !");
        Terminal.sautDeLigne();
      } catch (JIncorrect j) {
        Terminal.ecrireStringln("j incorrect !");
        Terminal.sautDeLigne();
      } catch (RayonIncorrect r) {
        Terminal.ecrireStringln("Rayon incorrect !");
        Terminal.sautDeLigne();
      } catch (LargeurIncorrecte l) {
        Terminal.ecrireStringln("Largeur incorrecte !");
        Terminal.sautDeLigne();
      } catch (HauteurIncorrecte h) {
        Terminal.ecrireStringln("Hauteur incorrecte !");
        Terminal.sautDeLigne();
      } catch (TerminalException t) {
        Terminal.ecrireStringln("Mauvaise saisie !");
        Terminal.sautDeLigne();
      }
    }
  }
}
class CouleurPasDefinie extends Exception{}
class EpaisseurTropGrande extends Exception{}
class IIncorrect extends Exception{}
class JIncorrect extends Exception{}
class RayonIncorrect extends Exception{}
class LargeurIncorrecte extends Exception{}
class HauteurIncorrecte extends Exception{}
