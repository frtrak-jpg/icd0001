import java.util.*;

/**
 * Container class to different classes, that makes the whole
 * set of classes one class formally.
 *
 * Ülesanne: Sidevõrgu kriitiliste ühenduslülide (sildade) tuvastamine.
 *
 * Sildade leidmiseks kasutatakse Tarjani algoritmi, mis põhineb
 * sügavuti läbimisel (DFS). Igale tipule omistatakse avastamisaeg
 * (disc) ja low-väärtus, mis tähistab madalaimat avastamisaega,
 * mis on antud tipust DFS-puu järglaste ja maksimaalselt ühe
 * tagasikaare kaudu kättesaadav. Serv (u, v) on sild parajasti
 * siis, kui DFS-puus lapse v low-väärtus on suurem kui vanema u
 * avastamisaeg.
 *
 * Orienteerimata graaf on esitatud kahesuunaliste kaarepaaridena
 * (kaared AB ja BA). Tulemuses sisalduvad kaarepaarid täies mahus,
 * st sildade kaartelist sisaldab nii AB kui ka BA, kui ühenduselõik
 * AB on sild. Sildade arv on aga unikaalsete servade arv (poole
 * võrra väiksem kaarte arvust).
 */
public class GraphTask {

   /** Main method. */
   public static void main (String[] args) {
      GraphTask a = new GraphTask();
      a.run();
   }

   /**
    * Tulemuse hoidja: leitud sildade kaarte loend ja unikaalsete
    * servade arv. List<Arc> sisaldab iga silla kohta mõlemat suunda
    * (AB ja BA), sest orienteerimata graafis on serv esitatud kahe
    * vastassuunalise kaarena.
    */
   public class BridgeResult {
      private final List<Arc> bridges;
      private final int bridgeCount;

      BridgeResult (List<Arc> bridges, int bridgeCount) {
         this.bridges = bridges;
         this.bridgeCount = bridgeCount;
      }

      public List<Arc> getBridges() { return bridges; }
      public int getBridgeCount() { return bridgeCount; }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();
         sb.append ("Sildu kokku: ").append (bridgeCount);
         sb.append (" (kaari listis: ").append (bridges.size()).append (")");
         if (!bridges.isEmpty()) {
            sb.append (", kaared: ");
            for (int i = 0; i < bridges.size(); i++) {
               if (i > 0) sb.append (", ");
               Arc x = bridges.get (i);
               sb.append (x.id).append ("(")
                  .append (x.source.id).append ("->")
                  .append (x.target.id).append (")");
            }
         }
         return sb.toString();
      }
   }

   /** Vertex (graafi tipp). Lisaks aluspõhjas pakutud väljadele on
    * Tarjani algoritmi vajadusteks lisatud avastamisaja (disc) ja
    * low-väärtuse väljad. Need on otseselt algoritmi tööväljad ning
    * neid kasutatakse ainult bridge-otsingu ajal.
    */
   class Vertex {

      private String id;
      private Vertex next;
      private Arc first;
      private int info = 0;
      // DFS-i tööväljad Tarjani algoritmi jaoks:
      private int disc = -1;   // avastamisaeg (-1 = veel külastamata)
      private int low = -1;    // madalaim kättesaadav disc

      Vertex (String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }
   }


   /** Arc represents one arrow in the graph. Two-directional edges are
    * represented by two Arc objects (for both directions).
    *
    * Lisaks aluspõhja väljadele on lisatud viit lähtetipule (source),
    * et tulemuse esitlemisel oleks kaare suuna kohta täielik info,
    * ning viit serva paariskaarele (twin), mis võimaldab Tarjani
    * algoritmis ohutult välistada DFS-i lõpust naasmise samale
    * servale (mitte ainult vanematipule, mis ei tööks paralleelsete
    * servade korral).
    */
   class Arc {

      private String id;
      private Vertex source;   // kaare lähtetipp
      private Vertex target;
      private Arc next;
      private Arc twin;        // sama serva vastassuunaline kaar
      private int info = 0;

      Arc (String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      Arc (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }
   }


   /** This header represents a graph. */
   class Graph {

      private String id;
      private Vertex first;
      private int info = 0;

      Graph (String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph (String s) {
         this (s, null);
      }

      @Override
      public String toString() {
         String nl = System.getProperty ("line.separator");
         StringBuffer sb = new StringBuffer (nl);
         sb.append (id);
         sb.append (nl);
         Vertex v = first;
         while (v != null) {
            sb.append (v.toString());
            sb.append (" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append (" ");
               sb.append (a.toString());
               sb.append (" (");
               sb.append (v.toString());
               sb.append ("->");
               sb.append (a.target.toString());
               sb.append (")");
               a = a.next;
            }
            sb.append (nl);
            v = v.next;
         }
         return sb.toString();
      }

      public Vertex createVertex (String vid) {
         Vertex res = new Vertex (vid);
         res.next = first;
         first = res;
         return res;
      }

      public Arc createArc (String aid, Vertex from, Vertex to) {
         Arc res = new Arc (aid);
         res.next = from.first;
         from.first = res;
         res.target = to;
         res.source = from;
         return res;
      }

      /**
       * Loo orienteerimata serv tippude from ja to vahel,
       * tehes kaks vastassuunalist kaart ja sidudes need
       * üksteisega twin-väljaga.
       * @param from üks serva ots
       * @param to teine serva ots
       */
      public void createEdge (Vertex from, Vertex to) {
         Arc a = createArc ("a" + from.id + "_" + to.id, from, to);
         Arc b = createArc ("a" + to.id + "_" + from.id, to, from);
         a.twin = b;
         b.twin = a;
      }

      /**
       * Create a connected undirected random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       * Iga uus serv on esitatud kahe vastassuunalise kaarena ning
       * paariskaared on omavahel seotud twin-väljaga.
       * @param n number of vertices added to this graph
       */
      public void createRandomTree (int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex [n];
         for (int i = 0; i < n; i++) {
            varray [i] = createVertex ("v" + String.valueOf(n-i));
            if (i > 0) {
               int vnr = (int)(Math.random()*i);
               Arc f = createArc ("a" + varray [vnr].toString() + "_"
                  + varray [i].toString(), varray [vnr], varray [i]);
               Arc b = createArc ("a" + varray [i].toString() + "_"
                  + varray [vnr].toString(), varray [i], varray [vnr]);
               f.twin = b;
               b.twin = f;
            }
         }
      }

      /**
       * Create an adjacency matrix of this graph.
       * Side effect: corrupts info fields in the graph
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int [info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res [i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Create a connected simple (undirected, no loops, no multiple
       * arcs) random graph with n vertices and m edges.
       * Iga serv on esitatud kahe vastassuunalise kaarena, mis on
       * üksteisega seotud twin-väljaga.
       * @param n number of vertices
       * @param m number of edges
       */
      public void createRandomSimpleGraph (int n, int m) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException ("Too many vertices: " + n);
         if (m < n-1 || m > n*(n-1)/2)
            throw new IllegalArgumentException
               ("Impossible number of edges: " + m);
         first = null;
         createRandomTree (n);       // n-1 edges created here
         Vertex[] vert = new Vertex [n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1;  // remaining edges
         while (edgeCount > 0) {
            int i = (int)(Math.random()*n);  // random source
            int j = (int)(Math.random()*n);  // random target
            if (i==j)
               continue;  // no loops
            if (connected [i][j] != 0 || connected [j][i] != 0)
               continue;  // no multiple edges
            Vertex vi = vert [i];
            Vertex vj = vert [j];
            Arc f = createArc ("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected [i][j] = 1;
            Arc b = createArc ("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected [j][i] = 1;
            f.twin = b;
            b.twin = f;
            edgeCount--;  // a new edge happily created
         }
      }

      /**
       * Põhimeetod: leia kõik sillad antud orienteerimata graafis
       * Tarjani algoritmiga.
       *
       * Algoritm:
       *   1) Iga külastamata tipu jaoks alusta DFS-puud.
       *   2) Sügavuti läbimisel omista igale tipule avastamisaeg
       *      disc ja low = disc.
       *   3) Iga lapstipu v kohta uuenda u.low = min(u.low, v.low).
       *      Kui v.low > u.disc, siis serv (u,v) on sild.
       *   4) Tagasikaare puhul (külastatud naaber, mis ei ole
       *      DFS-puu vanem) uuenda u.low = min(u.low, naaber.disc).
       *
       * Implementatsioon on iteratiivne (kasutab Deque'i), et
       * 2000+ tipu puhul vältida JVM-i rekursioonipinu ülevoolu.
       * Mitmekordsete servade korral oleks tavalise "ära mine
       * vanema poole tagasi" reegli asemel ohutum kasutada
       * twin-välja (et mitte sama serva mööda tagasi minna),
       * mida me siin ka teeme.
       *
       * Eeldab, et iga orienteerimata serv on esitatud kahe
       * twin-välja kaudu seotud kaarepaarina.
       *
       * @return BridgeResult: kaarte loend (mõlemad suunad iga silla
       *         kohta) ja unikaalsete sildade arv
       */
      public BridgeResult findBridges() {
         List<Arc> resultArcs = new ArrayList<>();

         // Lähtesta DFS-väljad. disc = -1 tähendab külastamata.
         Vertex v = first;
         while (v != null) {
            v.disc = -1;
            v.low = -1;
            v = v.next;
         }

         int[] timer = new int[]{0};

         // Kui graaf ei pruugi olla sidus, läbime kõik tipud.
         v = first;
         while (v != null) {
            if (v.disc == -1) {
               iterativeDfsBridges (v, timer, resultArcs);
            }
            v = v.next;
         }

         // resultArcs sisaldab nüüd iga silla kohta mõlemat suunda
         // (twin-paariga lisatud), seega unikaalsete servade arv on
         // kaartelisti pikkuse pool.
         return new BridgeResult (resultArcs, resultArcs.size() / 2);
      }

      /**
       * Iteratiivne DFS, mis leiab sillad alustades komponendi
       * juurest 'root'. Kasutame paralleelseid stäkke: iga kaader
       * koosneb tipust u, kaarest mille kaudu u-sse tuldi
       * (parentArc, NO_PARENT kui pole) ja iteraatorist u
       * kaartelisti üle.
       */
      private void iterativeDfsBridges (Vertex root, int[] timer,
                                        List<Arc> resultArcs) {
         // Kasutame LinkedList-i (mitte ArrayDeque), sest
         // iteraatorstäkk peab lubama null-väärtusi (kui u
         // on viimane kaar läbi vaadatud, on järgmine "kaar"
         // null).
         Deque<Vertex> nodeStack = new LinkedList<>();
         Deque<Arc>    parentStack = new LinkedList<>();
         Deque<Arc>    iterStack = new LinkedList<>();
         // Marker "vanemkaart pole" parentStack'i jaoks,
         // et eristada juurtippu sisemistest tippudest.
         final Arc NO_PARENT = new Arc ("__none__");

         root.disc = timer[0];
         root.low  = timer[0];
         timer[0]++;
         nodeStack.push (root);
         parentStack.push (NO_PARENT);
         iterStack.push (root.first); // järgmine kaar, mida vaadata

         while (!nodeStack.isEmpty()) {
            Vertex u = nodeStack.peek();
            Arc parentArc = parentStack.peek();
            Arc nextArc = iterStack.peek();

            if (nextArc != null) {
               // Eemalda praegune iteraator stäkist ja lükka tagasi
               // nihutatud iteraator (s.t. järgmine kaar).
               iterStack.pop();
               iterStack.push (nextArc.next);

               Vertex w = nextArc.target;

               // Ära mine sama serva kaudu tagasi vanemtipu juurde.
               // Twin-välja kasutamine töötab ka mitmekordsete
               // servade korral korrektselt.
               if (parentArc != NO_PARENT && nextArc == parentArc.twin) {
                  continue;
               }

               if (w.disc == -1) {
                  // Avasta uus tipp; lisa uus kaader stäkkile.
                  w.disc = timer[0];
                  w.low  = timer[0];
                  timer[0]++;
                  nodeStack.push (w);
                  parentStack.push (nextArc);
                  iterStack.push (w.first);
               } else {
                  // Tagasikaar (back edge): uuenda u.low.
                  if (w.disc < u.low) {
                     u.low = w.disc;
                  }
               }
            } else {
               // Kõik u-st väljuvad kaared on läbi vaadatud:
               // pop ja levita low üles vanemale.
               nodeStack.pop();
               parentStack.pop();
               iterStack.pop();

               if (parentArc != NO_PARENT) {
                  Vertex p = parentArc.source;
                  // Levita low vanemale
                  if (u.low < p.low) {
                     p.low = u.low;
                  }
                  // Sild parajasti siis, kui u.low > p.disc
                  if (u.low > p.disc) {
                     // Lisa mõlemad suunad: parentArc (p->u) ja
                     // tema twin (u->p).
                     resultArcs.add (parentArc);
                     if (parentArc.twin != null) {
                        resultArcs.add (parentArc.twin);
                     }
                  }
               }
            }
         }
      }
   }


   // ====================================================================
   // run() ja katsenäited
   // ====================================================================

   /** Actual main method to run examples and everything. */
   public void run() {
      System.out.println ("==== Sildade leidmine orienteerimata graafis ====");
      System.out.println ();

      // -- Demonstreeriv väike näide ----------------------------------
      // Käsitsi koostatud graaf, mille kohta on lihtne kontrollida,
      // millised servad on sillad:
      //
      //      A --- B --- C --- D --- E --- F
      //                              |   /
      //                              |  /
      //                              | /
      //                              G
      //
      // Sillad: A-B, B-C, C-D, D-E.
      // Tsükkel E-F-G-E omavahelised servad ei ole sillad.
      Graph demo = new Graph ("Demo");
      Vertex vA = demo.createVertex ("A");
      Vertex vB = demo.createVertex ("B");
      Vertex vC = demo.createVertex ("C");
      Vertex vD = demo.createVertex ("D");
      Vertex vE = demo.createVertex ("E");
      Vertex vF = demo.createVertex ("F");
      Vertex vG = demo.createVertex ("G");
      demo.createEdge (vA, vB);
      demo.createEdge (vB, vC);
      demo.createEdge (vC, vD);
      demo.createEdge (vD, vE);
      demo.createEdge (vE, vF);
      demo.createEdge (vF, vG);
      demo.createEdge (vG, vE);

      System.out.println ("Antud graaf:");
      System.out.println (demo);
      System.out.println ("Eeldatavad sillad (käsitsi): A-B, B-C, C-D, D-E");
      BridgeResult demoRes = demo.findBridges();
      System.out.println ("Tulemus: " + demoRes);
      System.out.println ();

      // -- Test 1: ahel (path) -- iga serv on sild ---------------------
      runTest ("Test 1: ahel 5 tipust (kõik servad sillad)",
               buildPath (5), 4);

      // -- Test 2: tsükkel -- ühtegi silda ei ole ----------------------
      runTest ("Test 2: tsükkel 6 tipust (mitte ühtegi silda)",
               buildCycle (6), 0);

      // -- Test 3: kaks tsüklit ühe sillaga ühendatud ------------------
      runTest ("Test 3: kaks kolmnurka ühendatud ühe sillaga",
               buildTwoTrianglesWithBridge(), 1);

      // -- Test 4: täielik graaf K5 -- ühtegi silda --------------------
      runTest ("Test 4: täielik graaf K5 (mitte ühtegi silda)",
               buildComplete (5), 0);

      // -- Test 5: täht -- iga "kiir" on sild --------------------------
      runTest ("Test 5: täht (5 lehega) -- iga kiir on sild",
               buildStar (5), 5);

      // -- Test 6: juhuslik puu 12 tipust ------------------------------
      // Puu on alati selline graaf, kus iga serv on sild.
      Graph randTree = new Graph ("RandTree");
      randTree.createRandomTree (12);
      runTest ("Test 6: juhuslik puu 12 tipust (iga serv sild)",
               randTree, 11);

      // -- Test 7: juhuslik tihe graaf 10 tipust, 25 servaga -----------
      Graph randDense = new Graph ("RandDense");
      randDense.createRandomSimpleGraph (10, 25);
      System.out.println ("Test 7: juhuslik tihe graaf 10 tipust, 25 servaga");
      System.out.println (randDense);
      BridgeResult r7 = randDense.findBridges();
      System.out.println ("Sildu: " + r7.getBridgeCount() + " tk");
      System.out.println ("Detailid: " + r7);
      System.out.println ();

      // -- Suure mahuga test (>= 2000 tippu) ---------------------------
      System.out.println ("==== Suuremahulised testid (>= 2000 tippu) ====");
      bigTest (2200, 5000);
      bigTest (2200, 2199);   // puu: kõik servad sillad
      bigTest (2500, 6000);
   }

   /** Käivita tavaline test, mis trükib graafi (kui pole liiga suur),
    * leiab sillad ja võrdleb arvu eeldatavaga.
    */
   private void runTest (String name, Graph g, int expectedBridges) {
      System.out.println (name);
      System.out.println (g);
      BridgeResult r = g.findBridges();
      System.out.println ("Sildade arv: " + r.getBridgeCount()
            + " (eeldus " + expectedBridges + ")  -- "
            + (r.getBridgeCount() == expectedBridges ? "OK" : "EBAÕIGE!"));
      System.out.println ("Detailid: " + r);
      System.out.println ();
   }

   /** Suure graafi test: ainult tippude/servade arvu ja töökiiruse
    * mõõtmine, ilma graafi sisu väljatrükita.
    */
   private void bigTest (int n, int m) {
      Graph big = new Graph ("Big");
      long t0 = System.nanoTime();
      big.createRandomSimpleGraph (n, m);
      long t1 = System.nanoTime();
      BridgeResult r = big.findBridges();
      long t2 = System.nanoTime();
      System.out.println ("Suuretest: n=" + n + ", m=" + m
            + ", sildu=" + r.getBridgeCount()
            + ", graafi loomine=" + ((t1 - t0) / 1_000_000) + " ms"
            + ", sildade leidmine=" + ((t2 - t1) / 1_000_000) + " ms");
   }

   // -- Abifunktsioonid kindla kujuga graafide ehitamiseks --------------

   /** Loo ahel n tipust: v1 - v2 - v3 - ... - vn. */
   private Graph buildPath (int n) {
      Graph g = new Graph ("Path" + n);
      Vertex prev = null;
      for (int i = 1; i <= n; i++) {
         Vertex v = g.createVertex ("v" + i);
         if (prev != null) g.createEdge (prev, v);
         prev = v;
      }
      return g;
   }

   /** Loo tsükkel n tipust. */
   private Graph buildCycle (int n) {
      Graph g = new Graph ("Cycle" + n);
      Vertex[] vs = new Vertex [n];
      for (int i = 0; i < n; i++) vs[i] = g.createVertex ("v" + (i+1));
      for (int i = 0; i < n; i++) g.createEdge (vs[i], vs[(i+1) % n]);
      return g;
   }

   /** Kaks kolmnurka, ühendatud ühe sillaga. */
   private Graph buildTwoTrianglesWithBridge() {
      Graph g = new Graph ("TwoTriangles");
      Vertex a = g.createVertex ("a1");
      Vertex b = g.createVertex ("a2");
      Vertex c = g.createVertex ("a3");
      Vertex d = g.createVertex ("b1");
      Vertex e = g.createVertex ("b2");
      Vertex f = g.createVertex ("b3");
      g.createEdge (a, b); g.createEdge (b, c); g.createEdge (c, a);
      g.createEdge (d, e); g.createEdge (e, f); g.createEdge (f, d);
      g.createEdge (c, d);   // ainus sild
      return g;
   }

   /** Täielik graaf Kn. */
   private Graph buildComplete (int n) {
      Graph g = new Graph ("K" + n);
      Vertex[] vs = new Vertex [n];
      for (int i = 0; i < n; i++) vs[i] = g.createVertex ("v" + (i+1));
      for (int i = 0; i < n; i++)
         for (int j = i+1; j < n; j++)
            g.createEdge (vs[i], vs[j]);
      return g;
   }

   /** Täht: keskpunkt + n lehte, iga serv on sild. */
   private Graph buildStar (int leaves) {
      Graph g = new Graph ("Star" + leaves);
      Vertex c = g.createVertex ("c");
      for (int i = 0; i < leaves; i++) {
         Vertex l = g.createVertex ("l" + i);
         g.createEdge (c, l);
      }
      return g;
   }
}
