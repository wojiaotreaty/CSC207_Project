package entity;

import java.time.LocalDate;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CommonTask implements Task {
    private String name;
    private String description;
    private LocalDate deadline;
    private boolean status;

    public CommonTask(String name, LocalDate deadline, String description) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        status = false;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getName() {
        return name;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
    public String getDescription() {
        return description;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonTask that = (CommonTask) o;
        return status == that.status && Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) && Objects.equals(deadline, that.deadline);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(name).append("`");
        s.append(description).append("`");
        s.append(deadline.toString()).append("`");
        s.append(status);
        return String.valueOf(s);
    }
}
