package com.medicalstore.controller;

import com.medicalstore.model.Order;
import com.medicalstore.model.OrderItem;
import com.medicalstore.repository.UserRepository;
import com.medicalstore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // View all orders (Admin, Supplier, Customer -> role restriction can be looser here)
    @GetMapping("/orders")
    public String viewOrders(Model model, Principal principal) {
        // For simplicity show all orders to logged in users; enforce role checks in template if needed.
        model.addAttribute("orders", orderService.listAll());
        return "orders/list";
    }

    // Checkout page - show blank form or prefill based on cart mechanism you use
    @GetMapping("/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String checkoutForm(Model model, Principal principal) {
        Order order = new Order();
        order.setUserId(null); // populated in POST from principal in your app
        // example: one empty item to render fields
        OrderItem it = new OrderItem();
        it.setQuantity(1);
        it.setPrice(BigDecimal.ZERO);
        List<OrderItem> items = new ArrayList<>();
        items.add(it);
        order.setItems(items);
        model.addAttribute("order", order);
        return "orders/checkout";
    }

    // Place new order
    @PostMapping("/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String placeOrder(@Valid @ModelAttribute("order") Order order,
                             BindingResult br,
                             Principal principal,
                             Model model) {
        if (br.hasErrors()) {
            model.addAttribute("order", order);
            return "orders/checkout";
        }

        // ✅ Map principal name → userId from DB
        String username = principal.getName();
        Integer userId = userRepository.findUserIdByUsername(username);
        order.setUserId(userId);

        order.setStatus("PENDING");
        Integer id = orderService.placeOrder(order);
        return "redirect:/order/" + id;
    }

    // View single order details (open to all authenticated)
    @GetMapping("/order/{id}")
    public String orderDetails(@PathVariable("id") Integer id, Model model) {
        Optional<Order> opt = orderService.getById(id);
        if (opt.isEmpty()) {
            return "redirect:/orders?notfound";
        }
        model.addAttribute("order", opt.get());
        return "orders/details";
    }

    // Update status - Admin or Supplier
    @PostMapping("/order/status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER')")
    public String updateStatus(@PathVariable("id") Integer id,
                               @RequestParam("status") String status) {
        orderService.updateStatus(id, status);
        return "redirect:/order/" + id;
    }
}
