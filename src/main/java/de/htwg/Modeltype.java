package de.htwg;

public enum Modeltype {
    COMMERCIAL(){
        @Override
        public String toString() {
            return "commercial";
        }
    }, OPEN_SOURCE(){
        @Override
        public String toString() {
            return "opensource";
        }

    }
}
