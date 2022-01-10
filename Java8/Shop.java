import java.util.HashMap;
import java.util.Map;

class Shop implements ShopInterface {

    Map <String, Object> BlockingMap = new HashMap<>();
    Map <String, Integer> GoodsInStock = new HashMap<>();

    @Override
    public void delivery(Map<String, Integer> goods) {
        for (var entry : goods.entrySet()){
            Integer AfterDelivery;
            synchronized (this){
                if (GoodsInStock.get(entry.getKey()) != null){
                    AfterDelivery = entry.getValue() + GoodsInStock.get(entry.getKey());
                }
                else{
                    AfterDelivery = entry.getValue();
                }

                GoodsInStock.put(entry.getKey(), AfterDelivery);
            
            }
            if(BlockingMap.get(entry.getKey()) != null){
                synchronized(BlockingMap.get(entry.getKey())){
                    BlockingMap.get(entry.getKey()).notifyAll();
                }
            }
        }
    }

    @Override
    public boolean purchase(String productName, int quantity)  {
        
        if (BlockingMap.get(productName) == null){
            BlockingMap.put (productName, new Object());
        }

        synchronized(BlockingMap.get(productName)){
            if(GoodsInStock.get(productName) == null) {
                try{
                    BlockingMap.get(productName).wait();
                }
                catch (InterruptedException e){
                    System.out.println(e.getLocalizedMessage());
                }
            }
            else if (GoodsInStock.get(productName) < quantity){
                try{
                    BlockingMap.get(productName).wait();
                }
                catch (InterruptedException e){
                    System.out.println(e.getLocalizedMessage());
                }
            }
            if(GoodsInStock.get(productName) >= quantity){
                Integer AfterBuy = GoodsInStock.get(productName) - quantity;
                GoodsInStock.put(productName, AfterBuy);
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Integer> stock() {
        return GoodsInStock;
    }
    
}