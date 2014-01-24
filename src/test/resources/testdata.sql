DROP TABLE IF EXISTS `attachment`;
CREATE TABLE IF NOT EXISTS `attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file` longblob,
  `creationDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `sha1` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `attachmentKey` (`creationDate`,`name`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `details`;
CREATE TABLE IF NOT EXISTS `details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room` varchar(45) DEFAULT NULL,
  `weekday` int(11) DEFAULT NULL,
  `startTime` time DEFAULT NULL,
  `endTime` time DEFAULT NULL,
  `interval` varchar(45) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `detailsKey` (`room`,`weekday`,`startTime`,`endTime`,`interval`,`startDate`,`endDate`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

INSERT INTO `details` (`id`, `room`, `weekday`, `startTime`, `endTime`, `interval`, `startDate`, `endDate`) VALUES
	(1, 'G16-H5', 1, '12:15:00', '13:45:00', 'P1Y', '2013-10-14', '2014-01-31'),
	(2, 'G16-H5', 4, '12:15:00', '13:45:00', 'P1Y', '2013-10-21', '2014-01-31'),
	(6, 'G29-E037', 2, '14:15:00', '15:45:00', 'P1Y', '2013-10-21', '2014-01-31'),
	(7, 'G29-E037', 2, '16:15:00', '17:45:00', 'P1Y', '2013-10-21', '2014-01-31'),
	(9, 'G29-E037', 3, '10:15:00', '11:45:00', 'P1Y', '2013-10-21', '2014-01-31'),
	(10, 'G29-E037', 3, '12:15:00', '13:45:00', 'P1Y', '2013-10-21', '2014-01-31'),
	(3, 'G29-K058', 1, '12:15:00', '13:45:00', 'P1Y', '2013-10-21', '2014-01-31'),
	(4, 'G29-K058', 1, '16:00:00', '17:30:00', 'P1Y', '2013-10-21', '2014-01-31'),
	(11, 'G29-K058', 3, '16:15:00', '17:45:00', 'P1Y', '2013-10-21', '2014-01-31'),
	(5, 'G29-K059', 2, '10:15:00', '11:45:00', 'P1Y', '2013-10-21', '2014-01-31'),
	(8, 'G29-K059', 3, '10:15:00', '11:45:00', 'P1Y', '2013-10-21', '2014-01-31');


DROP TABLE IF EXISTS `lecture`;
CREATE TABLE IF NOT EXISTS `lecture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lectureKey` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `lecture` (`id`, `name`) VALUES
	(1, 'Einführung in die Informatik');


DROP TABLE IF EXISTS `lectureDetails`;
CREATE TABLE IF NOT EXISTS `lectureDetails` (
  `lecture_id` int(11) NOT NULL,
  `additionalInfo_id` int(11) NOT NULL,
  PRIMARY KEY (`lecture_id`,`additionalInfo_id`),
  KEY `fk_lectureDetails_additionalInfo1_idx` (`additionalInfo_id`),
  KEY `fk_lectureDetails_lecture1_idx` (`lecture_id`),
  CONSTRAINT `fk_lectureDetails_lecture1` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_lectureDetails_additionalInfo1` FOREIGN KEY (`additionalInfo_id`) REFERENCES `details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `lectureDetails` (`lecture_id`, `additionalInfo_id`) VALUES
	(1, 1),
	(1, 2);

DROP TABLE IF EXISTS `lectureGroup`;
CREATE TABLE IF NOT EXISTS `lectureGroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `maxMember` int(11) DEFAULT NULL,
  `submissionDeadlineTime` time DEFAULT NULL,
  `submissionDeadlineWeekday` tinyint(4) DEFAULT NULL,
  `lecture` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lectureGroupIndex` (`name`,`lecture`),
  KEY `fk_lectureGroup_lecture1_idx` (`lecture`),
  KEY `lectureGroupKey` (`name`),
  CONSTRAINT `fk_lectureGroup_lecture1` FOREIGN KEY (`lecture`) REFERENCES `lecture` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO `lectureGroup` (`id`, `name`, `maxMember`, `submissionDeadlineTime`, `submissionDeadlineWeekday`, `lecture`) VALUES
	(1, 'Gruppe 1', 24, '11:00:00', 1, 1),
	(2, 'Gruppe 2', 22, '11:00:00', 1, 1),
	(3, 'Gruppe 3', 22, '11:00:00', 1, 1),
	(5, 'Gruppe 4', 22, '11:00:00', 1, 1),
	(6, 'Gruppe 5', 22, '11:00:00', 1, 1),
	(7, 'Gruppe 6', 24, '11:00:00', 1, 1),
	(8, 'Gruppe 7', 23, '11:00:00', 1, 1),
	(9, 'Gruppe 8', 22, '11:00:00', 1, 1),
	(10, 'Gruppe 9', 22, '11:00:00', 1, 1);

DROP TABLE IF EXISTS `lectureGroupDetails`;
CREATE TABLE IF NOT EXISTS `lectureGroupDetails` (
  `group_id` int(11) NOT NULL,
  `additionalInfo_id` int(11) NOT NULL,
  PRIMARY KEY (`group_id`,`additionalInfo_id`),
  KEY `fk_lectureGroupDetails_additionalInfo1_idx` (`additionalInfo_id`),
  KEY `fk_lectureGroupDetails_group1_idx` (`group_id`),
  CONSTRAINT `fk_lectureGroupDetails_group1` FOREIGN KEY (`group_id`) REFERENCES `lectureGroup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_lectureGroupDetails_additionalInfo1` FOREIGN KEY (`additionalInfo_id`) REFERENCES `details` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `lectureGroupDetails` (`group_id`, `additionalInfo_id`) VALUES
	(1, 3),
	(2, 4),
	(3, 5),
	(5, 6),
	(6, 7),
	(7, 8),
	(8, 9),
	(9, 10),
	(10, 11);

DROP TABLE IF EXISTS `lectureGroupTaskSheet`;
CREATE TABLE IF NOT EXISTS `lectureGroupTaskSheet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `defaultDeadline` datetime DEFAULT NULL,
  `defaultReleaseDate` datetime DEFAULT NULL,
  `taskSheet` int(11) DEFAULT NULL,
  `lectureGroup` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_lectureGroupTaskSheet_taskSheet2_idx` (`taskSheet`),
  KEY `fk_lectureGroupTaskSheet_lectureGroup1_idx` (`lectureGroup`),
  CONSTRAINT `fk_lectureGroupTaskSheet_taskSheet2` FOREIGN KEY (`taskSheet`) REFERENCES `taskSheet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_lectureGroupTaskSheet_lectureGroup1` FOREIGN KEY (`lectureGroup`) REFERENCES `lectureGroup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `task`;
CREATE TABLE IF NOT EXISTS `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `description` longtext,
  `isAutomaticTest` tinyint(1) DEFAULT '0',
  `backend` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`),
  KEY `title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

INSERT INTO `task` (`id`, `title`, `description`, `isAutomaticTest`, `backend`) VALUES
	(1, 'Perfekte Zahl', 'Eine <strong>natürliche Zahl</strong> <em>n</em> heißt perfekt oder vollkommen, wenn die Summe ihrer Teiler gleich n ist. Dabei sind die Teiler echt kleiner als <em>n</em>\r\n<p>. Beispielsweise ist 6 = 1 + 2 + 3 eine perfekte Zahl.</p>\r\n<p>&nbsp;</p>\r\nGeben Sie einen Algorithmus als <strong>Pseudocode</strong> an, der für eine gegebene Zahl bestimmt, ob sie perfekt ist.', 0, NULL),
	(2, 'Kubikwurzel nach Heron', 'Erweitern Sie die Lösungsidee für die Berechnung der Quadratwurzel nach Heron (siehe Vorlesungsfolien, Teil01, Folie 10) für die Berechnung der Kubikwurzel. Beschreiben Sie diesen Algorithmus im <strong>Pseudocode</strong>\r\n<p>.</p>\r\n<p>&nbsp;</p>\r\n<p>\r\nTipp:</p>\r\nAnstatt der Länge der Seite eines Quadrats mit Flächeninhalt <em>a</em> (Quadratwurzel in Vorlesung) benötigt man für die dritte Wurzel von <em>v</em> die Länge der Seite eines Kubus mit Rauminhalt <em>v</em>. Entwerfen Sie einen Algorithmus <strong>kubik</strong>, der einen Quader solange verändert bis annähernd alle Seiten gleich lang sind.', 0, NULL),
	(4, 'Primzahl', '<ol type="a">\r\n<li>Schreiben Sie eine Java-Methode \r\n<br /><br /><strong>public static boolean isPrime(int n)</strong>,<br /><br />\r\ndie möglichst effizient (siehe Blatt0, Aufgabe Primzahl) entscheidet, ob eine <strong>natürliche Zahl</strong> <em>n</em> eine Primzahl ist. Die Methode soll <em>true</em> zurückgeben, falls <em>n</em> eine Primzahl ist, im anderen Fall <em>false</em>.\r\n<p>&nbsp;</p>\r\n</li><li>Schreiben Sie eine weitere Methode \r\n<br /><br /><strong>public static int nextPrime(int n)</strong>,<br /><br />\r\ndie für eine  <strong>natürliche Zahl</strong> <em>n</em> die kleinste Primzahl liefert, die größer oder gleich <em>n</em> ist.</li></ol>', 0, NULL),
	(5, 'Backus-Naur-Form (BNF)', '<ol type="a" start="1">\r\nSchreiben Sie in Backus-Naur-Form (BNF) gültige deutsche und amerikanische Datumsangaben (erst Monat, dann Tag, siehe unten).\r\n<br />Hinweis: Zur Vereinfachung werden folgende Annahmen getroffen:\r\n<ul>\r\n<li>Alle Monate können 31 Tage haben, die Tage 1 bis 9 werden ohne führende Null angegeben.</li>\r\n<li>Monatsangaben werden ebenfalls ohne führende Null geschrieben.</li>\r\n<li>Die Jahresangabe sei grundsätzlich vierstellig.</li>\r\n<li>Es ist sowohl die deutsche als auch amerikanische Schreibweise zulässig.</li>\r\n<li>Gültige Datumsangaben sind:\r\n<br />3.11.2006\r\n<br />9.1.2007\r\n<br />11-3-2006 oder 11/3/2006\r\n<br />1-9-2007 oder 1/9/2007\r\n<br />2006-11-3 oder 2006/11/3\r\n<br />2007-1-9 oder 2007/1/9\r\n</li></ul>\r\n</ol>', 0, NULL),
	(6, 'Median', '<ol type="a">   \r\n<li>Schreiben Sie eine Java-Methode\r\n<br /><br /><strong>public static int median(int a, int b, int c)</strong>, <br /><br />\r\ndie von drei gegebenen ganzen Zahlen den <em>Median</em> (also den mittleren Wert in der sortierten Folge) der drei Zahlen ermittelt und zurück liefert.\r\n<p>Beispiel: <em>median(25, 11, 33)</em> ergibt <em>25</em></p>\r\n</li>\r\n<li>Geben Sie noch eine alternative Variante zur Bestimmung des Medians als Java-Methode <em>median2</em> an.</li>\r\n<li>Wie sieht ein möglicher Testrahmen für Ihre Funktion <em>median</em> aus? Reichen Sie eine entsprechende main-Methode ein.\r\n<br />Hinweis: Die main-Methode wird nicht automatisch geprüft aber in der Übung diskutiert.</li></ol>', 0, NULL),
	(7, 'Zahlenpalindrome', '<p>Palindrome sind Zeichenketten, die von vorn und hinten gelesen das \r\nGleiche ergeben. Bsp.: Lagerregal. Gleiches funktioniert auch mit natürlichen Zahlen. So ist 94549 ein Zahlenpalindrom.<br />\r\nZiel ist nun bei einer gegebenen <strong>natürlichen Zahl</strong> (Dezimalzahl &gt; 0) zu bestimmen, ob sie ein Palindrom ist oder nicht. Entwickeln Sie dazu zunächst folgende Hilfsfunktionen, die bei der Lösung nützlich seien können.</p>\r\n<ol type="a" start="1">\r\n<li> Schreiben Sie eine Java-Methode<br />\r\n  <br />\r\n  <strong>public static int numDigits(int n)</strong>,<br />\r\n  <br />\r\ndie die Anzahl an Dezimalstellen einer natürlichen Zahl ausgibt.<br />\r\nBsp.: numDigits(42) ergibt 2<br /></li>\r\n  <br />\r\n<li> Als nächstes wäre ein Zugriff auf einzelne Ziffern hilfreich. Schreiben Sie dazu eine Java-Methode<br />\r\n  <br />\r\n  <strong>public static int getDigit(int n, int index)</strong>,<br />\r\n  <br />\r\ndie die i-te Dezimalziffer einer natürlichen Zahl ausgibt. Die Ziffern werden von <strong>hinten</strong> bei 0 begonnen zu zählen. Die erste Ziffer ist also die mit dem Index 0.<br />\r\nBsp.: getDigit(2012, 1) ergibt 1<br />\r\n<br /> Für die allgemeine Nutzung der Methode <em>getDigit</em> ist es sinnvoll, bei fehlerhaften Indizes folgendermaßen vorzugehen:  Ist der Index kleiner Null soll die 0te Ziffer zurückgeliefert werden. Ist der Index größer als die Ziffernfolge lang ist, soll die letzte Ziffer (beachte Zählung beginnt hinten!) zurückgeliefert werden.\r\n</li>  <br />\r\n<li> Schreiben Sie nun die Java-Methode<br />\r\n  <strong><br />\r\npublic static boolean isPalindrome(int n)</strong>,<br /><br />\r\ndie für eine natürliche Zahl bestimmt, ob sie ein Palindrom ist oder nicht.\r\n</li></ol>', 0, NULL),
	(8, 'Umrechnung von Dezimal- in Dualzahl', '<p>Schreiben Sie eine Java-Methode</p>\r\n<p>&nbsp;</p>\r\n<strong>public static String transformDual(int dec)</strong>\r\n<p>&nbsp;</p>\r\n<p>&nbsp;</p>\r\nzur Umwandlung einer beliebigen natürlichen Zahl <em>dec</em>\r\n<p> (dec&gt;0) in eine Dualzahl.\r\nTesten Sie diese Methode an verschiedenen Beispielen.</p>\r\nBeispiel: <em>transformDual(13)</em> liefert <em>"1101"</em>\r\n<p>&nbsp;</p>\r\n<p>&nbsp;</p>\r\n<p>\r\nHinweis:</p>\r\nBenutzen Sie zur Lösung dieser Aufgaben <strong>keine</strong> Standardmethoden wie z. B. <em>Integer.toBinaryString(...)</em>', 0, NULL),
	(10, 'Zahlenspielerei MyIntArray', 'Schreiben Sie eine Klasse <em>MyIntArray</em> mit verschiedenen Methoden:\r\n<ol type="a" start="1">\r\n<li>Die Methode\r\n<br /><br /><strong>public static int[] createRandom(int n)</strong><br /><br />\r\nbekommt die Größe des zu erzeugenden Feldes übergeben, die Elemente des Feldes sollen mit ganzzahligen Zufallszahlen aus dem Bereich 5 bis einschließlich 99 belegt werden.\r\n<br />\r\n<br />Hinweis: Nutzen Sie zum Generieren der Zufallszahlen die Standardfunktion\r\n<em>random</em> aus der Klasse <em>Math</em>. Mit der Anweisung\r\n<div align="center"><em>int z = (int)(maxrand*Math.random());</em></div>\r\nwird eine Zufallszahl <em>z</em> zwischen <em>0</em> und <em>maxrand</em> erzeugt (0 &lt;= z &lt; maxrand).\r\n</li>\r\n<li><br /><strong>public static String toString(int[] a)</strong></li>\r\nsoll die Elemente von <em>a</em> in einem String zurück liefern. Testen Sie in <em>main</em> Ihr <em>toString(...)</em> und geben Sie das Feld auch mit der vordefinierten Methode <em>java.util.Arrays.toString(...)</em> aus.\r\n<li><br /><strong>public static int posMin(int[] a)</strong><br /><br />\r\nsoll die Position der kleinsten Zahl in <em>a</em> liefern. Wenn das minimale Element mehrmals vorkommt, soll nur die Position des <strong>ersten</strong> Auftretens ermittelt werden.</li>\r\n<li><br /><strong>public static void swap(int[] a)</strong><br /><br />\r\nvertauscht das erste mit dem letzten Element des Feldes <em>a</em>.</li>\r\n<ol></ol>\r\n</ol>', 0, NULL),
	(11, 'Befreundete Zahlen', '<p>Als befreundete Zahlen (amicable numbers) werden Paare natürlicher Zahlen (n, m) mit n ≠ m bezeichnet, für die gilt:</p>\r\n<p>\r\nDie Summe der echten Teiler von n ergibt m und Summe der echten Teiler von m ergibt n.</p>\r\n<p>\r\nBitte beachten Sie, dass auch 1 als Teiler von n betrachtet und mitsummiert wird, die Zahl n selbst aber nicht; beispielsweise ist die Summe der Teiler von 6 gleich 1 + 2 + 3.</p>\r\n<p>\r\nSchreiben Sie eine Java-Methode</p>\r\n<strong>public static int[][] amicablePairs(int n),</strong>\r\n<p>&nbsp;</p>\r\n<p>&nbsp;</p>\r\ndie <em>n</em>\r\n<p> Paare befreundeter Zahlen liefert (Ergebnis ist eine zweispaltige Matrix).</p>\r\n<p>&nbsp;</p>\r\nHinweise:\r\n<ul>\r\n<li>Bei der Berechnung der befreundeten Paare ist darauf zu achten, dass die erste Zahl im jeweiligen Tupel die kleinere ist und keine Dubletten auftauchen.</li>\r\n<li>Beachten Sie beim Testen, dass die Ermittlung der befreundeten Paare mitunter viel Rechenzeit benötigt. Es ist daher ratsam, das <em>n</em> recht <strong>klein</strong> zu halten.</li>\r\n<li>Diskutieren Sie dabei verschiedene Implementierungsvarianten. </li></ul>', 0, NULL),
	(12, 'Perfektes Mischen', 'Sogenanntes perfektes Mischen eines Kartenstapels mit einer <strong>geraden</strong>\r\n<p>\r\nAnzahl von Karten funktioniert wie folgt:</p>\r\nDer Kartenstapel wird in der Mitte geteilt, dann werden die beiden Hälften so ineinander gemischt, dass die Karten aus den beiden Hälften genau abwechselnd im gemischten Stapel vorkommen.\r\n<p>&nbsp;</p>\r\n<p>Beispiel: Der ursprüngliche Stapel enthalte Zehn, Bube,Dame, König, As, Joker. Die zwei Hälften enthalten dann einerseits Zehn, Bube, Dame und andererseits König, As, Joker. Nach dem Zusammenfügen ergibt sich die gemischte Reihenfolge Zehn, König, Bube, As, Dame, Joker.</p>\r\n<p>Der Einfachheit halber repräsentieren wir den Kartenstapel dabei als Feld\r\nvon natürlichen Zahlen.</p>\r\n<ol type="a" start="1">\r\n<li>Schreiben Sie zunächst eine  Methode\r\n<br /><strong>public static int[] interleave(int[] a1, int[] a2)</strong>,<br />\r\ndie zwei als gleich lang vorausgesetzte Felder <em>a1</em> und <em>a2</em> akzeptiert (muss nicht überprüft werden) und ein Feld zurückgibt, in dem die Elemente aus <em>a1</em> und <em>a2</em> abwechselnd vorkommen, wobei mit dem ersten Element von <em>a1</em> begonnen wird.\r\nBeispiel\r\n<pre>int[] a1 = {1,2,3};\r\nint[] a2 = {4,5,6};\r\nint [] c = interleave(a,b); c enthält die Elemente 1,4,2,5,3,6</pre>\r\n</li>\r\n<li>Schreiben Sie eine  weitere Methode \r\n<br /><strong>public static int[] perfectMerge(int[] a)</strong>,<br /> \r\ndie perfektes Mischen für Felder mit einer geraden Anzahl von Elementen realisiert. Dazu muss zunächst das übergebene Feld in der Mitte geteilt werden und danach <em>interleave</em> aufgerufen werden.<br />\r\nBeispiel:\r\n<pre>int[] a = {1,2,3,4}\r\nperfectMerge(a) ergibt dann ein Feld mit den Elementen 1,3,2,4</pre>\r\n</li>\r\n<li>\r\nWird ein Kartenspiel oft genug perfekt gemischt, kehrt es in seine ursprüngliche Reihenfolge zurück. Schreiben Sie nun eine Methode \r\n<br /><strong>public static int mergeNumber(int n)</strong>, <br />\r\ndie eine beliebige gerade Zahl grösser Null als Grösse eines Kartenstapels akzeptiert und die zurückgibt, wie oft ein Kartenstapel dieser Größe höchstens perfekt gemischt werden muss, damit er seine ursprüngliche Reihenfolge wieder annimmt (wobei mindestens einmal gemischt werden muss).\r\n<br />\r\nBeispiel:\r\n<pre>mergeNumber(52) ergibt 8</pre>\r\n</li></ol>', 0, NULL),
	(13, 'Rekursive Funktion interpretieren I', 'Es ist folgende Funktion gegeben:\r\n<pre>Algorithm: f(x,y)\r\nInput:     x,y ganzzahlig\r\nOutput:    ?\r\n   if y = 0 then x\r\n            else f(x-1, y-1)\r\n   fi</pre>\r\n<ol type="a" start="1">\r\n<li>Beschreiben Sie die Auswertung an mehreren Beispielen. Was berechnet diese Funktion? Für welche Werte terminiert der Algorithmus nicht?</li>\r\n<li>Überlegen Sie wie der Algorithmus zu erweitern ist, damit er für <em>alle</em> ganzzahligen Werte terminiert. Schreiben Sie dafür eine Java-Methode \r\n<br /><br /><strong>public static int rek(int x, int y)</strong>.<br /><br /></li>\r\n<li>Formulieren Sie nun Ihren verbesserten Algorithmus als <em>iterative</em> Methode\r\n<br /><br /><strong> public static int iter(int x, int y)</strong>.<br /><br /> </li></ol>', 0, NULL),
	(14, 'Rekursive Funktion interpretieren II', '<ol type="a" start="1">\r\n\r\n<li>Gegeben sei der folgende rekursive Algorithmus für ganze Zahlen <em>n</em>:\r\n<pre>Algorithm: f(n)\r\nInput:     n ganzzahlig\r\nOutput:    ?\r\n   if n = 1  then 1\r\n             else f(n-1) + 2*n - 1\r\n   fi</pre>\r\nErläutern Sie anhand jeweils eines kurzen Beispiels, welche Berechnung der Algorithmus in den folgenden beiden Fällen ausführt:\r\n<br /><br /><em>n &gt; 0  </em> und <em>n &lt; = 0</em><br /><br />\r\n</li>\r\n<li>Gegeben sei der folgende rekursive Algorithmus für ganze Zahlen <em>x</em> und <em>y</em> (div bedeutet ganzzahlige Division):\r\n<pre>Algorithm: f(x, y)\r\nInput:     x, y ganzzahlig\r\nOutput:    ?\r\n   if y = 0  then 1\r\n             else if(x &lt; 2 * y) then f(x, x - y)\r\n                                else x * f(x - 1, y - 1) div y\r\n                  fi\r\n   fi</pre>\r\n<ul>\r\n<li>Erläutern Sie anhand der Beispiele <em>f(4,2)</em> und <em>f(8,5)</em>, welche Berechnungen der Algorithmus ausführt.</li>\r\n<li>Für welche <em>x</em> und <em>y</em> terminiert der Algorithmus nicht?</li>\r\n<li>Was ist die Semantik des Algorithmus?</li></ul>\r\n</li></ol>', 0, NULL),
	(15, 'Hornerschema', 'Reelle Polynome beliebigen Grades <em>n</em> können als Arrays der Länge <em>n + 1</em>\r\n<p>\r\nrepräsentiert werden:</p>\r\n<p>&nbsp;</p>\r\n<img src="polynom.png" alt="" width="90%/" />\r\n<p>&nbsp;</p>\r\n<ol type="a" start="1">\r\n<li>Schreiben Sie eine Methode\r\n<br /><br /><strong>public static double evalSimple(double[] a, double x)</strong>,<br /><br />\r\ndie durch sukzessive Summation der Ausdrücke <em>a<sub>i</sub>x<sup>i</sup></em>, <em>0</em><em>≤i</em><em>≤n</em> den Funktionswert <em>f(x)</em> für eine Variable <em>x </em>berechnet. Wieviele Schleifendurchläufe werden benötigt (in Abhängigkeit von n)?\r\n<br />\r\nHinweis: Sie dürfen nur Addition und Multiplikation verwenden.</li>\r\n\r\n<li>Schreiben Sie eine weitere iterative Methode\r\n<br /><br /><strong>public static double evalHorner(double[] a, double x)</strong>,<br /><br />\r\nzur Berechnung von <em>f(x)</em> nach dem Hornerschema, d. h. nach der Formel\r\n<p><em>f(x) = (((...((a<sub>n</sub>x + a<sub>n-1</sub>)x + a<sub>n-2</sub>)...)x + a<sub>2</sub>)x + a<sub>1</sub>)x + a<sub>0</sub>.</em></p>\r\n</li><li>Bestimmen Sie die Anzahl der Schleifendurchläufe, die mit dem Horner-Verfahren benötigt werden. Vergleichen Sie die Algorithmen aus <em>a)</em> und <em>b)</em> miteinander.</li>\r\n<li>Schreiben Sie eine rekursive Variante des Hornerschemas als Methode\r\n<br /><br /><strong>public static double evalHornerRek(double[] a, double x)</strong><br /><br /></li></ol>', 0, NULL),
	(16, 'Variante der Ackermann-Funktion', 'Die folgende Funktion berechnet <strong>eine Version</strong> (nach Abelson, Sussman: Structure and Interpretation of Computer Programs) der mathematischen Funktion mit dem Namen <em>Ackermann</em>:\r\n<pre>Algorithm Ackermann (x, y):\r\n   Input: natuerliche Zahlen x, y\r\n   Output: Ackermann-Zahl\r\n   \r\n   if (y = 0) then 0\r\n   if (x = 0) then 2*y\r\n   if (y = 1) then 2\r\n   else Ackermann(x-1, Ackermann(x, y-1))</pre>\r\n<ol type="1">\r\n<li>Schreiben Sie eine Java-Methode\r\n<br /><br /> <strong>public static BigInteger ackermann(BigInteger x, BigInteger y)</strong><br /><br />\r\nzur Berechnung der Ackermann-Zahl. Hinweis: Informieren Sie sich über die Klasse <em>BigInteger</em> in der <a class="external-link" href="http://download.oracle.com/javase/6/docs/api/index.html">Java-API</a> (http://download.oracle.com/javase/6/docs/api/index.html).</li>\r\n<li>Welches sind die Werte der Methodenaufrufe <em>ackermann(1, 10), ackermann(2, 4), ackermann(3, 3)</em>?\r\n</li>\r\n\r\n<li>Die Klasse <em>BigInteger</em> dient der Verarbeitung beliebig großer ganzer Zahlen. Warum gibt es bei der Berechnung der Ackermann-Zahl trotzdem Probleme?</li></ol>', 0, NULL),
	(17, 'Fibonacci-Zahlen', 'Eine besondere Folge von Zahlen sind die Fibonacci-Zahlen, die rekursiv definiert werden können als\r\n<p>&nbsp;</p>\r\n<table>\r\n<tbody>\r\n<tr>\r\n<td><em>fib</em>(x) =</td>\r\n<td>if (x = 0) ∨ (x = 1) then 1</td>\r\n</tr>\r\n<tr>\r\n<td></td>\r\n<td>else <em>fib</em>(x-2) + <em>fib</em>(x-1)</td>\r\n</tr>\r\n<tr>\r\n<td></td>\r\n<td>fi</td>\r\n</tr>\r\n</tbody>\r\n</table>\r\n<ol type="a" start="1">\r\n<li>Schreiben Sie eine rekursive Methode \r\n<br /><br /><strong>public static BigInteger fib1(int n)</strong><br /><br />\r\nzur Berechnung der x-ten Fibonacci-Zahl.\r\nGeben Sie im Hauptprogramm die ersten 15 Fibonacci-Zahlen aus.</li>\r\n<li>Zeigen Sie am Beispiel <em>fib1(5)</em>, wie die Anzahl der Aufrufe dieser Methode wächst (Baumrekursion) (Lösung als Block-Kommentar).</li>\r\n<li>Formulieren Sie eine iterative Methode \r\n<br /><br /><strong>public static BigInteger fib2(int n)</strong><br /><br />\r\nzur Berechnung der Fibonacci-Zahlen und zeigen Sie am Beispiel <em>fib2(5)</em>, dass <em>fib2</em> effizienter arbeitet als <em>fib1</em>.\r\n<br /> Hinweis: Merken Sie sich jeweils die beiden Vorgänger in gesonderten Variablen.</li>\r\n<li>Berechnen Sie innerhalb der main-Methode nach beiden Varianten die Fibonacci-Zahl von <em>n = 23</em> und zählen Sie dabei die notwendigen Schleifendurchläufe.</li></ol>', 0, NULL),
	(18, 'Russische Bauernmultiplikation', 'Die Russische Bauernmultiplikation (auch Ägyptisches Multiplizieren oder Abessinische Bauernregel genannt) ist ein einfaches Verfahren zur Multiplikation zweier <strong>natürlicher Zahlen</strong>.\r\n<p>\r\n\r\nEs war schon im Altertum bekannt, in Deutschland wurde es bis ins Mittelalter verwendet. In Russland war es bis weit in die Neuzeit üblich, daher der Name.</p>\r\nDas Verfahren hat den Vorteil, dass man im Prinzip nur halbieren, verdoppeln und addieren muss.\r\n<div>&nbsp;\r\n<p>Die Berechnung erfolgt nach folgendem Algorithmus:</p>\r\n<ol type="1" start="1">\r\n<li>Man schreibt die beiden zu multiplizierenden Zahlen nebeneinander.</li>\r\n<li>Auf der linken Seite werden die Zahlen jeweils halbiert (Reste abgerundet) und die Ergebnisse untereinander geschrieben, bis man zur 1 gelangt.</li>\r\n<li>Auf der rechten Seite werden die Zahlen jeweils verdoppelt und untereinander geschrieben.</li>\r\n<li>Die rechts stehenden (verdoppelten) Zahlen werden gestrichen, wenn die links stehende Zahl gerade ist.</li>\r\n<li>Die Summe der nicht gestrichenen rechts stehenden Zahlen ergibt das gesuchte Produkt.</li></ol>\r\n<p>\r\nÜberprüfen Sie diesen Algorithmus an einem selbst gewählten Beispiel.</p>\r\nSchreiben Sie in Java eine&nbsp;<strong>iterative</strong>&nbsp;Methode&nbsp;<br /><br /><strong>public static int farmerMultIter(int x, int y)</strong><br /><br />und eine&nbsp;<strong>rekursive</strong>&nbsp;Methode&nbsp;<br /><br /><strong>public static int farmerMultRek(int x, int y)</strong><br /><br />für diesen Algorithmus.\r\n<div>&nbsp;</div>\r\n</div>', 0, NULL);

DROP TABLE IF EXISTS `taskAttachments`;
CREATE TABLE IF NOT EXISTS `taskAttachments` (
  `task_id` int(11) NOT NULL,
  `attachment_id` int(11) NOT NULL,
  PRIMARY KEY (`task_id`,`attachment_id`),
  KEY `fk_taskAttachments_attachment1_idx` (`attachment_id`),
  KEY `fk_taskAttachments_task1_idx` (`task_id`),
  CONSTRAINT `fk_taskAttachments_task1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_taskAttachments_attachment1` FOREIGN KEY (`attachment_id`) REFERENCES `attachment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taskPattern`;
CREATE TABLE IF NOT EXISTS `taskPattern` (
  `task_id` int(11) NOT NULL,
  `text_id` int(11) NOT NULL,
  PRIMARY KEY (`task_id`,`text_id`),
  KEY `fk_taskPattern_text1_idx` (`text_id`),
  KEY `fk_taskPatternt_task1_idx` (`task_id`),
  CONSTRAINT `fk_taskPattern_task1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_taskPattern_text1` FOREIGN KEY (`text_id`) REFERENCES `text` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `taskPattern` (`task_id`, `text_id`) VALUES
	(4, 1),
	(6, 2),
	(7, 3),
	(8, 4),
	(10, 5),
	(11, 6),
	(12, 7),
	(13, 8),
	(15, 9),
	(16, 10),
	(17, 11),
	(18, 12);

DROP TABLE IF EXISTS `taskSheet`;
CREATE TABLE IF NOT EXISTS `taskSheet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`),
  KEY `taskSheetKey` (`creationDate`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

INSERT INTO `taskSheet` (`id`, `title`, `creationDate`) VALUES
	(1, 'Blatt01', '2014-01-24 00:18:21'),
	(2, 'Blatt02', '2014-01-24 00:18:36'),
	(3, 'Blatt03', '2014-01-24 00:18:44'),
	(4, 'Blatt04', '2014-01-24 00:18:52');

DROP TABLE IF EXISTS `taskSheetTasks`;
CREATE TABLE IF NOT EXISTS `taskSheetTasks` (
  `taskSheet_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  PRIMARY KEY (`taskSheet_id`,`task_id`),
  KEY `fk_taskSheetTasks_task1_idx` (`task_id`),
  KEY `fk_taskSheetTasks_taskSheet1_idx` (`taskSheet_id`),
  CONSTRAINT `fk_taskSheetTasks_taskSheet1` FOREIGN KEY (`taskSheet_id`) REFERENCES `taskSheet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_taskSheetTasks_task1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `taskSheetTasks` (`taskSheet_id`, `task_id`) VALUES
	(1, 1),
	(1, 2),
	(1, 4),
	(1, 5),
	(2, 6),
	(2, 7),
	(2, 8),
	(2, 10),
	(3, 11),
	(3, 12),
	(3, 13),
	(3, 14),
	(4, 15),
	(4, 16),
	(4, 17),
	(4, 18);

DROP TABLE IF EXISTS `taskSubmission`;
CREATE TABLE IF NOT EXISTS `taskSubmission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `task` int(11) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `statusText` longtext,
  PRIMARY KEY (`id`),
  KEY `fk_taskSubmission_task1_idx` (`task`),
  KEY `submissionKey` (`date`,`status`,`statusText`(50)),
  CONSTRAINT `fk_taskSubmission_task1` FOREIGN KEY (`task`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taskSubmissionAttachment`;
CREATE TABLE IF NOT EXISTS `taskSubmissionAttachment` (
  `taskSubmission_id` int(11) NOT NULL,
  `attachment_id` int(11) NOT NULL,
  PRIMARY KEY (`taskSubmission_id`,`attachment_id`),
  KEY `fk_taskSubmission_has_attachment_attachment1_idx` (`attachment_id`),
  KEY `fk_taskSubmission_has_attachment_taskSubmission1_idx` (`taskSubmission_id`),
  CONSTRAINT `fk_taskSubmission_has_attachment_taskSubmission1` FOREIGN KEY (`taskSubmission_id`) REFERENCES `taskSubmission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_taskSubmission_has_attachment_attachment1` FOREIGN KEY (`attachment_id`) REFERENCES `attachment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taskSubmissionDates`;
CREATE TABLE IF NOT EXISTS `taskSubmissionDates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deadline` datetime DEFAULT NULL,
  `releasedate` datetime DEFAULT NULL,
  `lectureGroupTaskSheet` int(11) DEFAULT NULL,
  `task` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_taskSubmissionDates_lectureGroupTaskSheet1_idx` (`lectureGroupTaskSheet`),
  KEY `fk_taskSubmissionDates_task1_idx` (`task`),
  CONSTRAINT `fk_taskSubmissionDates_lectureGroupTaskSheet1` FOREIGN KEY (`lectureGroupTaskSheet`) REFERENCES `lectureGroupTaskSheet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_taskSubmissionDates_task1` FOREIGN KEY (`task`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taskSubmissionText`;
CREATE TABLE IF NOT EXISTS `taskSubmissionText` (
  `taskSubmission_id` int(11) NOT NULL,
  `text_id` int(11) NOT NULL,
  PRIMARY KEY (`taskSubmission_id`,`text_id`),
  KEY `fk_taskSubmission_has_text_text1_idx` (`text_id`),
  KEY `fk_taskSubmission_has_text_taskSubmission1_idx` (`taskSubmission_id`),
  CONSTRAINT `fk_taskSubmission_has_text_taskSubmission1` FOREIGN KEY (`taskSubmission_id`) REFERENCES `taskSubmission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_taskSubmission_has_text_text1` FOREIGN KEY (`text_id`) REFERENCES `text` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `taskTests`;
CREATE TABLE IF NOT EXISTS `taskTests` (
  `task_id` int(11) NOT NULL,
  `text_id` int(11) NOT NULL,
  PRIMARY KEY (`task_id`,`text_id`),
  KEY `fk_task_has_text_text1_idx` (`text_id`),
  KEY `fk_task_has_text_task1_idx` (`task_id`),
  CONSTRAINT `fk_task_has_text_task1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_task_has_text_text1` FOREIGN KEY (`text_id`) REFERENCES `text` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `text`;
CREATE TABLE IF NOT EXISTS `text` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `creationDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `sha1` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `textKey` (`creationDate`,`name`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

INSERT INTO `text` (`id`, `content`, `creationDate`, `name`, `type`, `sha1`) VALUES
	(1, 'public class Prime {\r\n\r\n      public static boolean isPrime(int n){\r\n      // hier bitte Quelltext einfuegen\r\n\r\n     }\r\n\r\n     public static int nextPrime(int n){\r\n     // hier bitte Quelltext einfuegen\r\n\r\n     }\r\n  \r\n     public static void main(String[] args) {\r\n     // hier bitte Testrahmen einfuegen\r\n\r\n    }\r\n}', '2014-01-23 23:59:51', 'Prime', 'java', 'f6c4c0f42e8f61733e5117f779ede24ada20974e'),
	(2, 'public class Median {\r\n   public static int median(int a, int b, int c){\r\n   // hier bitte Quelltext einfuegen\r\n\r\n   }\r\n   public static int median2(int a, int b, int c){\r\n   // hier bitte Quelltext einfuegen\r\n\r\n   }\r\n   public static void main(String[] args) {\r\n   // hier bitte Testrahmen einfuegen\r\n   }\r\n}', '2014-01-24 00:02:16', 'Media', 'java', '33bfd6329fa61a2dc1c6d80fc62d04176a5e87c5'),
	(3, 'public class NPalindrome{\r\n\r\n   public static int numDigits(int n) {\r\n      // hier Quelltext einfuegen\r\n   }\r\n\r\n   public static int getDigit(int n, int index){\r\n      // hier Quelltext einfuegen	\r\n   }\r\n	\r\n   public static boolean isPalindrome(int n){\r\n      // hier Quelltext einfuegen\r\n   }\r\n  \r\n   public static void main(String[] args) {\r\n     // hier bitte Testrahmen einfuegen\r\n\r\n   }\r\n}\r\n\r\n', '2014-01-24 00:02:58', 'NPalindrome', 'java', 'f838d70c6de4e7a2a55556ecf84adaff33dc0761'),
	(4, 'public class Transformation {\r\n   public static String transformDual(int dec) {\r\n       // hier bitte Quelltext einfuegen\r\n\r\n   }\r\n \r\n   public static void main(String[] args) {\r\n      // hier bitte Testrahmen einfuegen\r\n\r\n   }\r\n}', '2014-01-24 00:03:49', 'Transformation', 'java', '1d62c4974c69c87c23be1b2241906389e09cdaec'),
	(5, 'public class MyIntArray {\r\n	public static int[] createRandom(int n) {\r\n           // hier bitte Quelltext einfuegen\r\n		\r\n	}\r\n	public static String toString(int[] a){\r\n           // hier bitte Quelltext einfuegen		\r\n		\r\n	}\r\n	public static int posMin(int[] a) {\r\n           // hier bitte Quelltext einfuegen\r\n		\r\n	}\r\n	public static void swap(int[] a) {\r\n           // hier bitte Quelltext einfuegen\r\n		\r\n	}\r\n}', '2014-01-24 00:06:25', 'MyIntArray', 'java', '6ce792395906653bf4fd27919afee42792082cde'),
	(6, 'public class MyIntArray {\r\n	public static int[] createRandom(int n) {\r\n           // hier bitte Quelltext einfuegen\r\n		\r\n	}\r\n	public static String toString(int[] a){\r\n           // hier bitte Quelltext einfuegen		\r\n		\r\n	}\r\n	public static int posMin(int[] a) {\r\n           // hier bitte Quelltext einfuegen\r\n		\r\n	}\r\n	public static void swap(int[] a) {\r\n           // hier bitte Quelltext einfuegen\r\n		\r\n	}\r\n}', '2014-01-24 00:10:07', 'SpecialNumbers', 'java', '6ce792395906653bf4fd27919afee42792082cde'),
	(7, 'public class PerfectMerge {\r\n	public static int[] interleave(int[] a1, int[] a2) {\r\n		// hier bitte Quelltext einreichen\r\n\r\n	}\r\n\r\n	public static int[] perfectMerge(int[] a) {\r\n               // hier bitte Quelltext einreichen\r\n		\r\n	}\r\n\r\n	public static int mergeNumber(int n) {\r\n		// hier bitte Quelltext einreichen\r\n\r\n	}\r\n\r\n        public static void main(String[] args) {\r\n		// hier bitte Testrahmen einreichen\r\n\r\n	}\r\n}', '2014-01-24 00:10:59', 'PerfectMerge', 'java', 'c16179e3738d4281db1129455d0c694d5203d2d8'),
	(8, '/**************************************\r\nin diesem Kommentar bitte Antwort zu Teil a) einfuegen\r\n\r\n***********************************************/\r\npublic class MyClass {\r\n\r\n    public static int rek(int x, int y) {\r\n    // hier bitte Quelltext einfuegen\r\n\r\n    }\r\n\r\n    public static int iter(int x, int y) {\r\n    // hier bitte Quelltext einfuegen\r\n\r\n    }\r\n  \r\n   public static void main(String[] args) {\r\n     // hier bitte Testrahmen einfuegen\r\n\r\n   }\r\n}', '2014-01-24 00:12:00', 'MyClass', 'java', '60ce6d2ff2754d63c4a028338f514a90ce5c5770'),
	(9, 'public class EvalPolynom {\r\n\r\n   public static double evalSimple(double[] a, double x){\r\n        // hier bitte Quelltext einfuegen\r\n    \r\n   }\r\n   public static double evalHorner(double[] a, double x){\r\n        // hier bitte Quelltext einfuegen\r\n    \r\n   }\r\n   public static double evalHornerRek(double[] a, double x){\r\n        // hier bitte Quelltext einfuegen\r\n    \r\n   }\r\n   public static void main (String args[]) {\r\n        // hier bitte Testrahmen einfuegen\r\n   }\r\n}\r\n', '2014-01-24 00:15:09', 'EvalPolynom', 'java', '2b5eec48f0fe1b5274bba5175c578a23bb3f2a81'),
	(10, 'import java.math.BigInteger;\r\n\r\npublic class MyAckermann {\r\n    public static BigInteger ackermann(BigInteger x, BigInteger y){\r\n      // hier bitte Quelltext einfuegen\r\n    }\r\n   \r\n    public static void main(String[] args) {\r\n      // hier bitte Testrahmen einfuegen\r\n\r\n   }\r\n}\r\n', '2014-01-24 00:16:05', 'MyAckermann', 'java', '3d920ee4c1b38c5512a8bb3defd599ae68206b76'),
	(11, 'import java.math.*;\r\n\r\npublic class Fibonacci {\r\n\r\n    public static BigInteger fib1(int n){\r\n        // hier bitte Quelltext der rekursiven Variante eingeben\r\n\r\n\r\n    }\r\n\r\n    public static BigInteger fib2(int n){\r\n        // hier bitte Quelltext der iterativen Variante eingeben\r\n\r\n\r\n    }\r\n    public static void main(String[] args) {\r\n      // hier bitte Testrahmen einfuegen\r\n\r\n    }\r\n\r\n}\r\n/************************************************\r\nhier bitte Aufgabenteil b) einfuegen\r\n\r\n**************************************************/', '2014-01-24 00:16:58', 'Fibonacci', 'java', '7679bccbdca231845ca7a5ca7e0244069e56a0d4'),
	(12, 'public class FarmerMult {\r\n   public static int farmerMultRek(int x, int y) {\r\n	// TODO: source code 	\r\n   }\r\n   \r\n   public static int farmerMultIter(int x, int y) {\r\n	// TODO: source code 		\r\n   }\r\n\r\n   public static void main(String[] args) {\r\n	// TODO: test data		\r\n   }\r\n}\r\n\r\n', '2014-01-24 00:17:29', 'FarmerMulti', 'java', '4f34b727cd74d177c727b53f7a9ab5daa6b798b5');
