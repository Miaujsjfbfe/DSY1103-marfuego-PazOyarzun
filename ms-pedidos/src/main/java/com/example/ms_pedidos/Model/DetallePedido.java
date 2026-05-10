package com.example.ms_pedidos.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del plato obtenido desde ms-menu
    private Long platoId;

    // Datos del plato guardados en el pedido
    private String nombrePlato;
    private Double precio;
    private Integer cantidad;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}