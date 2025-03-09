package hse.dss.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks", schema = "dss")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{task.timeLimit.notnull}")
    @Min(value = 250, message = "{task.timeLimit.min}")
    @Max(value = 15000, message = "{task.timeLimit.max}")
    @Column(name = "time_limit", nullable = false)
    private Integer timeLimit;

    @NotNull(message = "{task.memoryLimit.notnull}")
    @Min(value = 4, message = "{task.memoryLimit.min}")
    @Max(value = 1024, message = "{task.memoryLimit.max}")
    @Column(name = "memory_limit", nullable = false)
    private Integer memoryLimit;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotBlank(message = "{task.name.notblank}")
    @Column(name = "name")
    private String name;

    @Column(name = "legend")
    private String legend;

    @Column(name = "input")
    private String input;

    @Column(name = "output")
    private String output;

    @Column(name = "generation_rules")
    private String generationRules;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test> tests;
}
