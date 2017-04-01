public class Country {
    private String name;
    private long population;
    private int area;
    private int index;


    public Country(int index, String name, long population, int area){
        this.index = index;
        this.name = name;
        this.population = population;
        this.area = area;
    }

    public int getArea() {
        return area;
    }

    public long getPopulation() {
        return population;
    }

    public String getName(){
        return name;
    }

    public double getDensity(){
        return this.population/this.area;
    }

    public int getIndex(){
        return index;
    }
}
