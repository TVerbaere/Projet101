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
+ ProjectFactory  		[en cours : AR]
+ ModelFactory 			[en cours : AR]
+ DiagramFactory 		[à voir si il faut garder : AR]

Tests unitaires :
-----------------
-----------------
+ Tests unitaires Helper	[en cours : TV]
+ Tests unitaires Handlers	[en cours : TV]
+ Tests unitaires Factories	[non affecté]
+ Tests d'intégrations		[affecté à AR]
