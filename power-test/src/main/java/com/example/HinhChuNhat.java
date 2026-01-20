package com.example;

public class HinhChuNhat {
    private final Diem topLeft;
    private final Diem bottomRight;

    public HinhChuNhat(Diem topLeft, Diem bottomRight) {
        if (topLeft == null || bottomRight == null) {
            throw new IllegalArgumentException("Invalid Data");
        }

        // topLeft phải nằm trên-trái: x nhỏ hơn, y lớn hơn
        if (topLeft.getX() >= bottomRight.getX() || topLeft.getY() <= bottomRight.getY()) {
            throw new IllegalArgumentException("Invalid Data");
        }

        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public double dienTich() {
        double width = bottomRight.getX() - topLeft.getX();
        double height = topLeft.getY() - bottomRight.getY();
        return width * height;
    }

    /**
     * Kiểm tra giao nhau (tính cả chạm cạnh là giao).
     * Nếu bạn muốn chạm cạnh KHÔNG tính giao, đổi <= thành < ở 4 điều kiện.
     */
    public boolean giaoNhau(HinhChuNhat other) {
        if (other == null)
            return false;

        double leftA = this.topLeft.getX();
        double rightA = this.bottomRight.getX();
        double topA = this.topLeft.getY();
        double bottomA = this.bottomRight.getY();

        double leftB = other.topLeft.getX();
        double rightB = other.bottomRight.getX();
        double topB = other.topLeft.getY();
        double bottomB = other.bottomRight.getY();

        // Không giao nhau nếu một cái nằm hoàn toàn bên trái/phải/trên/dưới cái kia
        boolean separated = rightA < leftB || rightB < leftA ||
                topA < bottomB || topB < bottomA;

        return !separated;
    }
}
