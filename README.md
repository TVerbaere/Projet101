#Projet 101

##Création programmatique de diagrammes Papyrus.

Etat du projet :
----------------

Implémentation :
----------------
----------------

+ UMLModelHelper		[OK : TV]
  + delete 			[OK]
+ AbstractDiagramHandler	[OK : TV]
  + getModel / getTED  		[OK]
  + setWidth			[OK]
  + setHeight			[OK]
  + getWidth			[OK]
  + getHeight			[OK]
  + getLocation			[OK]
  + setLocation			[OK]
  + getElementViewByName	[OK]
  + getViewByElement	 	[OK]
  + getElementByName		[OK]
  + delete			[OK]
  + autoSize			[OK : même comportement que celui de papyrus]
  + draw (avec location)	[OK]
  + drawInside 			[OK]
  + draw (sans location) 	[OK]
+ ProjectFactory  		[OK : AR]
+ ModelFactory 			[OK : AR]
+ DiagramFactory 		[OK : AR]
+ Supprimer (ou plutôt integrer) othersources et revoir les factories pour les loads.

Tests unitaires :
-----------------
-----------------
+ Tests unitaires Helper	[en cours(90%) : TV] - Il faut les factories pour finaliser (setUp)!
+ Tests unitaires Handlers	[en cours(40%) : TV]
+ Tests unitaires Factories	[non affecté]
+ Tests d'intégrations		[en cours(50%) : AR] - Recrèer un plugin pour les tests (Test sauvegardé à la racine du projet)
+ Tester pour les autres types de diagrammes (Activité...)


+ Rapport