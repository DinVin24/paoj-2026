DROP TABLE IF EXISTS articole_comanda;
DROP TABLE IF EXISTS comenzi;
DROP TABLE IF EXISTS preparate;
DROP TABLE IF EXISTS restaurante;
DROP TABLE IF EXISTS soferi;
DROP TABLE IF EXISTS clienti;

CREATE TABLE clienti (
    id INTEGER PRIMARY KEY,
    nume TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    telefon TEXT,
    strada TEXT,
    oras TEXT,
    cod_postal TEXT,
    tara TEXT,
    puncte_fidelitate INTEGER DEFAULT 0
);

CREATE TABLE soferi (
    id INTEGER PRIMARY KEY,
    nume TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    telefon TEXT,
    strada TEXT,
    oras TEXT,
    cod_postal TEXT,
    tara TEXT,
    vehicul TEXT,
    numar_inmatriculare TEXT,
    disponibil INTEGER DEFAULT 1
);

CREATE TABLE restaurante (
    id INTEGER PRIMARY KEY,
    nume TEXT NOT NULL,
    tip_bucatarie TEXT,
    rating REAL DEFAULT 0.0,
    strada TEXT,
    oras TEXT,
    cod_postal TEXT,
    tara TEXT
);

CREATE TABLE preparate (
   id INTEGER PRIMARY KEY,
   nume TEXT NOT NULL,
   descriere TEXT,
   pret REAL NOT NULL,
   disponibil INTEGER DEFAULT 1,
   id_restaurant INTEGER NOT NULL,
   FOREIGN KEY(id_restaurant) REFERENCES restaurante(id) ON DELETE CASCADE
);

CREATE TABLE comenzi (
     id INTEGER PRIMARY KEY,
     id_client INTEGER NOT NULL,
     id_restaurant INTEGER NOT NULL,
     id_sofer INTEGER,
     status TEXT NOT NULL,
     total REAL NOT NULL,
     data_crearii TEXT NOT NULL,
     FOREIGN KEY(id_client) REFERENCES clienti(id),
     FOREIGN KEY(id_restaurant) REFERENCES restaurante(id),
     FOREIGN KEY(id_sofer) REFERENCES soferi(id)
);

CREATE TABLE articole_comanda (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      id_comanda INTEGER NOT NULL,
      id_preparat INTEGER NOT NULL,
      cantitate INTEGER NOT NULL,
      FOREIGN KEY(id_comanda) REFERENCES comenzi(id) ON DELETE CASCADE,
      FOREIGN KEY(id_preparat) REFERENCES preparate(id)
);
