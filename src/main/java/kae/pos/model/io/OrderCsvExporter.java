package kae.pos.model.io;

import kae.pos.model.entity.Order;
import kae.pos.model.entity.OrderItem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OrderCsvExporter {

    public void export(List<Order> orders, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("OrderId,Date,Items,Total");
            writer.newLine();

            for (Order order : orders) {
                String items = order.getItems().stream()
                        .map(OrderItem::toString)
                        .reduce((a, b) -> a + " | " + b)
                        .orElse("");

                String line = String.format("%d,%s,\"%s\",%.2f",
                        order.getId(),
                        order.getDate(),
                        items,
                        order.getTotalPrice());

                writer.write(line);
                writer.newLine();
            }
        }
    }
}