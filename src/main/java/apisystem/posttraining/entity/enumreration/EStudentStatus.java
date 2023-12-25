package apisystem.posttraining.entity.enumreration;

public enum EStudentStatus {
    dang_hoc("Đang học"),
    da_tot_nghiep("Đã tốt nghiệp"),
    tot_nghiep_tam_thoi("Tốt nghiệp tạm thời"),
    bao_luu("Bảo lưu"),
    thoi_hoc("Thôi học");

    private final String name;

    /**
     * @param name
     */
    private EStudentStatus(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
