# Tank-Game

This is a small game project write in Java ( JavaSwing )

![alt text](https://github.com/dodoproptit99/Tank-Game/blob/master/intro.png)

## Requirements.

* JDK8 trở lên
* Netbean IDE
* Cài java trong netbean

## Dành cho người chơi.

Để chơi game, hãy vào file /src/main/Start.java và ấn run. 

![alt_text](https://github.com/dodoproptit99/Tank-Game/blob/master/image/Screenshot%20from%202019-05-07%2021-20-00.png)

Có 2 chế độ chơi, chơi một người và hai người.

Luật chơi rất đơn giản, hạ hết tank địch và bảo vệ thành trì trước khi bị giết để chiến thắng.

## Dành cho người muốn phát triển game.

#### 1. Đồ họa

Mình làm như sau, lấy sẵn hình ảnh các chuyển động (4 hướng) của các tank, các hình ảnh về wall, river, base và để trong thư mục image. Khi run game, graphics sẽ liên tục vẽ lại các hình ảnh đó dựa vào FRESHTIME. FRESHTIME càng nhanh, tank di chuyển càng nhanh (tốc độ vẽ càng nhanh), và ngược lại.

Các file code đồ họa đều nằm trong thư mục frame. 

Nhiệm vụ của các file này là tạo giao diện cho game, căn chỉnh cửa sổ game, các kích thước, tốc độ game. Chi tiết gồm có: nhận các KeyEvent (nhận nút từ bàn phím) và xử lí gọi các hàm di chuyển của từng đối tượng, draw ra màn hình. 

#### 2. Mô hình

Mình cấu hình từng đối tượng riêng trong game: Map, bullet, tank, boom, bot tank. Bot tank sẽ được kế thừa từ Tank.

Về tank, các thông số như tốc độ đạn, số mạng, thời gian chờ sau mỗi lần bắn được setup trong phần này:

``` java
	private boolean attackCoolDown = true;// Attack cooling
	private int attackCoolDownTime = 500;// Attack cooldown, milliseconds

	protected boolean hasBullet;// Check whether the tank bullet exists and cannot continue to fire bullets when it exists.

	private int life;
```
Bot tank có một đặc điểm khác là tự động di chuyển theo hàm random:

``` java
private Direction randomDirection() {
		Direction[] dirs = Direction.values();//Get the enumeration value of the direction
		Direction oldDir = dir;
		Direction newDir = dirs[random.nextInt(4)];
		if (oldDir == newDir || newDir == Direction.UP) {
// If the original direction of the computer tank is the same as the random direction, or if the new direction of the computer tank is up, then re-randomly redirect the direction.
			return dirs[random.nextInt(4)];
		}
		return newDir;
	}
  ```
  
  #### 3. Âm thanh
  
  Các file nhạc nền và âm thanh hiệu ứng được lưu trong phần audio. File ulti sẽ có nhiệm vụ đọc file ảnh và file nhạc vào game. 
  
  #### 4. Tổng kết
  
  Vì trong code mình có comment từng phần nên sẽ chỉ giới thiệu sơ qua về cấu trúc game. Các bạn đọc comment để hiểu rõ hơn từng phần nhé. 
  
Code được viết dựa trên sự hỗ trợ của một tài khoản Trung Quốc.
