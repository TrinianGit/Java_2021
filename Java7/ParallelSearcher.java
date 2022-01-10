import java.util.ArrayList;
import java.util.List;

class ParallelSearcher implements ParallelSearcherInterface{

    HidingPlaceSupplierSupplier BoxesSupplier;
    HidingPlaceSupplier BoxGiver;
    Double ValueToReturn = 0.0;

    @Override
    public void set(HidingPlaceSupplierSupplier supplier) {
        BoxesSupplier = supplier;
        CreateThreads();
    }

    public class SearchingWorker extends Thread{
        public void run(){
            HidingPlaceSupplier.HidingPlace Box;
            while (true){
                Box = BoxGiver.get();
                if (Box == null){
                    break;
                }
                else{
                    synchronized(Box){
                        if (Box.isPresent()){
                            synchronized(BoxGiver){
                                ValueToReturn += Box.openAndGetValue();
                            }
                        } 
                    }
                }
            }
        }
    }


    public void CreateThreads(){
        List <SearchingWorker> WorkersList = new ArrayList<SearchingWorker>();
        TakeNewBoxes();
        while(BoxGiver != null){
            for (int i = 0; i < BoxGiver.threads(); i++){
                SearchingWorker Worker = new SearchingWorker();
                WorkersList.add(Worker);
                Worker.start();
            }
            for (int i = 0; i < BoxGiver.threads(); i++){
                try{
                    WorkersList.get(i).join();
                }
                catch (Exception e){
                }
            }
            WorkersList.clear();
            TakeNewBoxes();
        }
    }

    public void TakeNewBoxes(){
        BoxGiver = BoxesSupplier.get(ValueToReturn);
        ValueToReturn = 0.0;
    }
}