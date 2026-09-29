package thanhdnh.ueh.edu.article_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ViewUserActivity extends AppCompatActivity {
  ImageView iv_detail;
  TextView tv_detail_id, tv_detail_title, tv_detail_email, tv_detail_hobby, tv_detail_description;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_user);
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }

    iv_detail = findViewById(R.id.iv_detail);
    tv_detail_id = findViewById(R.id.tv_detail_id);
    tv_detail_title = findViewById(R.id.tv_detail_title);
    tv_detail_email = findViewById(R.id.tv_detail_email);
    tv_detail_hobby = findViewById(R.id.tv_detail_hobby);
    tv_detail_description = findViewById(R.id.tv_detail_description);

    int id = getIntent().getIntExtra("id", 0);
    UserProfile user = UserData.getUserFromId(id);

    if (user != null) {
      if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {
        Picasso.get()
                .load(user.getAvatar_url())
                .resize(400, 400)
                .centerCrop()
                .into(iv_detail);
      }

      if (tv_detail_id != null) tv_detail_id.setText("ID: " + user.getId());
      if (tv_detail_title != null) tv_detail_title.setText(user.getUsername());
      if (tv_detail_email != null) tv_detail_email.setText("Email: " + user.getEmail());

      // Nếu bạn dùng getHobbies() hoặc getBio() thay cho getHobby(), hãy sửa lại tên ở đây cho khớp
      if (tv_detail_hobby != null) {
        tv_detail_hobby.setText("Sở thích: " + user.getHobby());
      }

      if (tv_detail_description != null) {
        tv_detail_description.setText("Mô tả: " + user.getDescription());
      }
    }
  }
}