package BierBest.order;

import BierBest.MainApp;
import BierBest.UpdateModelsTask;
import javassist.bytecode.stackmap.TypeData;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDetailsDisplayViewModel {
    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());
    OrderViewModel orderViewModel;

    public OrderDetailsDisplayViewModel() {
        this.orderViewModel = new OrderViewModel();
    }

    public OrderViewModel getOrderViewModel() {
        return orderViewModel;
    }

    public void updateOrder(String newStatus, String newPrice) {
        orderViewModel.getOrder().setStatusShopSide(newStatus);
        orderViewModel.getOrder().getBeerInfo().setPriceString(newPrice);

        final UpdateModelsTask updateModelsTask = new UpdateModelsTask(MainApp.sessionFactory, orderViewModel.getOrder());
        updateModelsTask.setOnSucceeded(ev -> LOGGER.log(Level.INFO, "order updated successfully"));
        Thread backgroundThread = new Thread(updateModelsTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();

        orderViewModel.loadDataFromOrderModel();
    }


    public void load(OrderViewModel newValue) {
        this.orderViewModel = newValue;
    }
}
