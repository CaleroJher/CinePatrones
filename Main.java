
//PATRON CREACIONAL:FACTORY METHOD

//Interfaz que define los datos que debe proporcionar una entrada
interface Entrada{
    String getDescripcion();
    double getPrecio();
}

//Representa una entrada normal con un precio de Bs 40.
class EntradaNormal implements Entrada{
    @Override
    public String getDescripcion(){
        return "Entrada Normal";
    }

    @Override
    public double getPrecio(){
        return 40;
    }
}

// Representa una entrada VIP con un precio de Bs 60
class EntradaVIP implements Entrada{
    @Override
    public String getDescripcion(){
        return "Entrada VIP";
    }

    @Override
    public double getPrecio(){
        return 60;
    }
}

//FACTORY METHOD:
//Esta clase define un metodo general para crear entradas
//Las clases hijas deciden que tipo de entrada se creara
abstract class CreadorEntrada{

    public abstract Entrada crearEntrada();
}

//Esta clase se encarga de crear entradas normales
class CreadorEntradaNormal extends CreadorEntrada{

    @Override
    public Entrada crearEntrada(){
        return new EntradaNormal();
    }
}

//Esta clase se encarga de crear entradas VIP
class CreadorEntradaVIP extends CreadorEntrada{

    @Override
    public Entrada crearEntrada(){
        return new EntradaVIP();
    }
}



//PATRON ESTRUCTURAL:DECORATOR

//DECORATOR:
//Esta clase sirve como base para agregar complementos a una entrada
//Guarda una entrada existente para que las clases hijas puedan
//agregar nuevos precios y descripciones sin modificar la entrada original
abstract class EntradaConExtras implements Entrada{
    protected Entrada entrada;

    public EntradaConExtras(Entrada entrada){
        this.entrada = entrada;
    }
}

//Agrega palomitas a cualquier tipo de entrada
//Utiliza el precio anterior y le suma Bs 15
class EntradaConPalomitas extends EntradaConExtras{

    public EntradaConPalomitas(Entrada entrada){
        super(entrada);
    }

    @Override
    public String getDescripcion(){
        return entrada.getDescripcion() + " + Palomitas";
    }

    @Override
    public double getPrecio(){
        return entrada.getPrecio() + 15;
    }
}

//Agrega una bebida a cualquier tipo de entrada
//Utiliza el precio anterior y le suma Bs 10
class EntradaConBebida extends EntradaConExtras{

    public EntradaConBebida(Entrada entrada){
        super(entrada);
    }

    @Override
    public String getDescripcion(){
        return entrada.getDescripcion() + " + Bebida";
    }

    @Override
    public double getPrecio(){
        return entrada.getPrecio() + 10;
    }
}


//PATRON DE COMPORTAMIENTO:STRATEGY

//STRATEGY:
//Esta interfaz define un metodo para calcular descuentos
//Cada clase que la implemente puede utilizar una forma
//diferente de calcular el precio final
interface Descuento{
    double aplicarDescuento(double precio);
}

//mantiene el precio original
class SinDescuento implements Descuento{

    @Override
    public double aplicarDescuento(double precio){
        return precio;
    }
}

//plica un descuento del 20% para estudiantes
class DescuentoEstudiante implements Descuento{

    @Override
    public double aplicarDescuento(double precio){
        return precio * 0.80;
    }
}



//CLASE ENCARGADA DE LA COMPRA

//Esta clase realiza la compra de una entrada
//Utiliza una estrategia de descuento recibida en el constructor
//Gracias a Strategy, no necesita saber como se calcula cada descuento
class Compra{
    private Descuento descuento;

    public Compra(Descuento descuento){
        this.descuento = descuento;
    }

    public void realizarCompra(String pelicula, Entrada entrada){

        double subtotal = entrada.getPrecio();
        double total = descuento.aplicarDescuento(subtotal);
        double descuentoAplicado = subtotal - total;

        
        System.out.println("COMPRA DE ENTRADAS");
        System.out.println("Pelicula: " + pelicula);
        System.out.println("Producto: " + entrada.getDescripcion());
        System.out.println("Subtotal: Bs " + subtotal);
        System.out.println("Descuento: Bs " + descuentoAplicado);
        System.out.println("Total a pagar: Bs " + total);
        System.out.println("Compra realizada exitosamente.");
        System.out.println();
    }
}


public class Main{
    public static void main(String[] args){
        //COMPRA 1:ENTRADA VIP

        //FACTORY METHOD:
        //Utilizamos CreadorEntradaVIP para crear una entrada VIP
        //La clase principal no necesita crear directamente EntradaVIP
        CreadorEntrada creadorVIP = new CreadorEntradaVIP();

        Entrada entradaVIP = creadorVIP.crearEntrada();

        //DECORATOR:
        //Agregamos palomitas y bebida a la entrada VIP
        //Cada complemento aumenta el precio sin cambiar EntradaVIP
        entradaVIP = new EntradaConPalomitas(entradaVIP);

        entradaVIP = new EntradaConBebida(entradaVIP);

        //STRATEGY:
        //Elegimos la estrategia que aplica un descuento del 20%
        Descuento descuentoEstudiante = new DescuentoEstudiante();

        //La clase Compra recibe la estrategia seleccionada
        Compra compraVIP = new Compra(descuentoEstudiante);

        compraVIP.realizarCompra("Avatar", entradaVIP);


        //COMPRA 2: ENTRADA NORMAL

        //FACTORY METHOD:
        //Utilizamos otro creador para obtener una entrada normal
        CreadorEntrada creadorNormal = new CreadorEntradaNormal();

        Entrada entradaNormal = creadorNormal.crearEntrada();

        //STRATEGY:
        //Elegimos una estrategia que no aplica descuentos
        Descuento sinDescuento = new SinDescuento();

        Compra compraNormal = new Compra(sinDescuento);

        compraNormal.realizarCompra("Titanic", entradaNormal);
    }
}