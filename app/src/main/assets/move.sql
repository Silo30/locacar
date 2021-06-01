CREATE TABLE TiposParticular (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  descricao VARCHAR
);

CREATE TABLE TiposPublico (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  descricao VARCHAR,
  PRIMARY KEY(_id)
);

CREATE TABLE MeioDeTransporte (
  _id INTEGER NOT NULL AUTOINCREMENT,
  descricao VARCHAR,
  PRIMARY KEY(_id)
);

CREATE TABLE TiposCompartilhado (
  _id INTEGER NOT NULL AUTOINCREMENT,
  descricao VARCHAR,
  PRIMARY KEY(_id)
);

CREATE TABLE TiposAlugado (
  _id INTEGER NOT NULL AUTOINCREMENT,
  descricao VARCHAR,
  PRIMARY KEY(_id)
);

CREATE TABLE TipoGasto (
  _id INTEGER NOT NULL AUTOINCREMENT,
  descricao VARCHAR,
  PRIMARY KEY(_id)
);

CREATE TABLE Usuario (
  _id INTEGER NOT NULL AUTOINCREMENT,
  nome VARCHAR NOT NULL,
  sobrenome VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  senha VARCHAR NOT NULL,
  facebook VARCHAR,
  sincronizar BOOL NOT NULL,
  PRIMARY KEY(_id)
);

CREATE TABLE Anuncio (
  _id INTEGER NOT NULL AUTOINCREMENT,
  anunciante VARCHAR,
  imagem VARCHAR,
  telefone VARCHAR,
  website VARCHAR,
  appURL VARCHAR,
  PRIMARY KEY(_id)
);

CREATE TABLE EstatisticasConta (
  Usuario_id INTEGER NOT NULL,
  qtdMeiosTransporte INTEGER,
  ultimoLogin DATETIME,
  PRIMARY KEY(Usuario_id)
  FOREIGN KEY(Usuario_id)
    REFERENCES Usuario(_id)
);

CREATE TABLE Evento (
  _id INTEGER NOT NULL AUTOINCREMENT,
  MeioDeTransporte_id INTEGER,
  PRIMARY KEY(_id)
  FOREIGN KEY(MeioDeTransporte_id)
    REFERENCES MeioDeTransporte(_id)
);

CREATE TABLE EstatisticasMeioTransporte (
  MeioDeTransporte_id INTEGER NOT NULL,
  media FLOAT,
  maximo FLOAT,
  minimo FLOAT,
  qtd INTEGER,
  PRIMARY KEY(MeioDeTransporte_id)
  FOREIGN KEY(MeioDeTransporte_id)
    REFERENCES MeioDeTransporte(_id)
);

CREATE TABLE Usuario_has_MeioDeTransporte (
  Usuario_id INTEGER NOT NULL,
  MeioDeTransporte_id INTEGER NOT NULL,
  PRIMARY KEY(Usuario_id, MeioDeTransporte_id)
  FOREIGN KEY(Usuario_id)
    REFERENCES Usuario(_id),
  FOREIGN KEY(MeioDeTransporte_id)
    REFERENCES MeioDeTransporte(_id)
);

CREATE TABLE Compartilhado (
  MeioDeTransporte_id INTEGER NOT NULL,
  TiposCompartilhado_id INTEGER NOT NULL,
  empresa VARCHAR,
  PRIMARY KEY(MeioDeTransporte_id)
  FOREIGN KEY(MeioDeTransporte_id)
    REFERENCES MeioDeTransporte(_id),
  FOREIGN KEY(TiposCompartilhado_id)
    REFERENCES TiposCompartilhado(_id)
);

CREATE TABLE Alugado (
  MeioDeTransporte_id INTEGER NOT NULL,
  TiposAlugado_id INTEGER NOT NULL,
  locadora VARCHAR,
  marca VARCHAR,
  modelo VARCHAR,
  cor VARCHAR,
  PRIMARY KEY(MeioDeTransporte_id)
  FOREIGN KEY(MeioDeTransporte_id)
    REFERENCES MeioDeTransporte(_id),
  FOREIGN KEY(TiposAlugado_id)
    REFERENCES TiposAlugado(_id)
);

CREATE TABLE Gasto (
  Evento_id INTEGER NOT NULL,
  TipoGasto_id INTEGER NOT NULL,
  valor NUMERIC,
  observacao VARCHAR,
  PRIMARY KEY(Evento_id)
  FOREIGN KEY(Evento_id)
    REFERENCES Evento(_id),
  FOREIGN KEY(TipoGasto_id)
    REFERENCES TipoGasto(_id)
);

CREATE TABLE Publico (
  MeioDeTransporte_id INTEGER NOT NULL,
  TiposPublico_id INTEGER NOT NULL,
  empresa VARCHAR,
  PRIMARY KEY(MeioDeTransporte_id)
  FOREIGN KEY(MeioDeTransporte_id)
    REFERENCES MeioDeTransporte(_id),
  FOREIGN KEY(TiposPublico_id)
    REFERENCES TiposPublico(_id)
);

CREATE TABLE Particular (
  MeioDeTransporte_id INTEGER NOT NULL,
  TiposParticular_id INTEGER NOT NULL,
  marca VARCHAR,
  modelo VARCHAR,
  cor VARCHAR,
  PRIMARY KEY(MeioDeTransporte_id)
  FOREIGN KEY(MeioDeTransporte_id)
    REFERENCES MeioDeTransporte(_id),
  FOREIGN KEY(TiposParticular_id)
    REFERENCES TiposParticular(_id)
);

CREATE TABLE Viagem (
  Evento_id INTEGER NOT NULL,
  inicio DATETIME,
  fim DATETIME,
  distancia FLOAT,
  PRIMARY KEY(Evento_id)
  FOREIGN KEY(Evento_id)
    REFERENCES Evento(_id)
);