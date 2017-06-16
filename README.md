![Alt text](/screenshots/screenshot3.png?raw=true "Screenshot")

# Obiektowość?
Całość podzieliłem na obiekty. Window odpowiada za stworzenie okna. Przyjmuje on obiekty Screen. Każdy Screen może w sobie przechowywać obiekty GLComponent, posiadające podstawowe metody renderowania oraz aktualizowania. 

# Klasy
 - Main - Główna klasa, uruchamia tylko okno
 - Window - Klasa tworząca okno oraz context OpenGL
 - Camera - Komponent stwarzający iluzję kamery, jest w nim sterowanie W/S/A/D/Q/E oraz tworzenie macierzy projekcji
 - GameScreen - Ekran, który posiada mapę
 - World - Komponent, który renderuje mapę
 - Texture - Obiekt, zajmujący się wczytywaniem, bindowaniem oraz niszczeniem tekstur
 - SimplexNoise oraz SimplexNoiseOctave to klasy, generujące losowy teren. Nie jest mojego autorstwa.