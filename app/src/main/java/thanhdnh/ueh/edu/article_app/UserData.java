package thanhdnh.ueh.edu.article_app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserData {
  public static UserList data;
  private final Context context;
  private final GridView gridview;
  private final ProgressBar progressBar;

  public UserData(Context context, GridView gridview) {
    this(context, gridview, null);
  }

  public UserData(Context context, GridView gridview, ProgressBar progressBar) {
    this.context = context;
    this.gridview = gridview;
    this.progressBar = progressBar;
  }

  public static UserProfile getUserFromId(int id) {
    if (data == null) return null;
    List<UserProfile> users = data.getUsers();
    if (users == null) return null;

    for (int i = 0; i < users.size(); i++) {
      UserProfile user = users.get(i);
      if (user != null && user.getId() == id) {
        return user;
      }
    }
    return null;
  }

  public static UserProfile getPhotoFromId(int id) {
    return getUserFromId(id);
  }

  public void loadData(String url, Activity activity) {
    Handler mainHandler = new Handler(Looper.getMainLooper());
    Downloader.downloadWithProgress(url, context.getCacheDir(), mainHandler, progressBar, new Downloader.ProgressListener() {
      @Override
      public void onProgressUpdate(int progress) {
      }

      @Override
      public void onComplete(File file) {
        if (file != null) {
          String jsonText = readText(file);
          parseAndSetData(jsonText, activity);
        } else {
          loadFallbackData(activity);
        }
      }

      @Override
      public void onError(Exception e) {
        loadFallbackData(activity);
      }
    });
  }

  private void parseAndSetData(String jsonText, Activity activity) {
    try {
      Gson gson = new Gson();
      if (jsonText != null && jsonText.trim().startsWith("[")) {
        Type listType = new TypeToken<ArrayList<UserProfile>>() {}.getType();
        ArrayList<UserProfile> userList = gson.fromJson(jsonText, listType);
        data = new UserList(userList);
      } else if (jsonText != null) {
        data = gson.fromJson(jsonText, UserList.class);
      }
      if (data != null && data.getUsers() != null && !data.getUsers().isEmpty()) {
        activity.runOnUiThread(() -> {
          UserAdapter adapter = new UserAdapter(data.getUsers(), context);
          gridview.setAdapter(adapter);
        });
      } else {
        loadFallbackData(activity);
      }
    } catch (Exception e) {
      e.printStackTrace();
      loadFallbackData(activity);
    }
  }

  private void loadFallbackData(Activity activity) {
    ArrayList<UserProfile> defaultUsers = new ArrayList<>();
    defaultUsers.add(new UserProfile(1, "Nguyễn Văn A", "nguyenvana@gmail.com", "Lập trình viên Android đam mê sáng tạo ứng dụng di động.", "https://i.pravatar.cc/300?img=1", "Đọc sách, Lập trình, Chơi game"));
    defaultUsers.add(new UserProfile(2, "Trần Thị B", "tranthib@gmail.com", "Chuyên gia thiết kế UI/UX với 5 năm kinh nghiệm.", "https://i.pravatar.cc/300?img=2", "Vẽ tranh, Nhiếp ảnh, Du lịch"));
    defaultUsers.add(new UserProfile(3, "Lê Hoàng C", "lehoangc@gmail.com", "Kỹ sư Backend yêu thích hệ thống phân tán và Cloud.", "https://i.pravatar.cc/300?img=3", "Nghe nhạc, Đá bóng, Chạy bộ"));
    defaultUsers.add(new UserProfile(4, "Phạm Minh D", "phamminhd@gmail.com", "Nhà phân tích dữ liệu đam mê AI và Machine Learning.", "https://i.pravatar.cc/300?img=4", "Cờ vua, Đọc sách, Bơi lội"));
    defaultUsers.add(new UserProfile(5, "Hoàng Anh E", "hoanganhe@gmail.com", "Quản lý dự án công nghệ thông tin trẻ trung và năng động.", "https://i.pravatar.cc/300?img=5", "Nấu ăn, Cầu lông, Xem phim"));
    defaultUsers.add(new UserProfile(6, "Vũ Đức F", "vuducf@gmail.com", "Lập trình viên Flutter & iOS nhiệt huyết.", "https://i.pravatar.cc/300?img=6", "Chơi guitar, Lập trình, Phượt"));
    defaultUsers.add(new UserProfile(7, "Đặng Thu G", "dangthug@gmail.com", "Kỹ sư đảm bảo chất lượng phần mềm (QA/QC).", "https://i.pravatar.cc/300?img=7", "Yoga, Làm bánh, Viết lách"));
    defaultUsers.add(new UserProfile(8, "Bùi Quốc H", "buiquoch@gmail.com", "Chuyên gia An ninh mạng và Bảo mật thông tin.", "https://i.pravatar.cc/300?img=8", "Giải đố, Leo núi, Công nghệ"));
    defaultUsers.add(new UserProfile(9, "Đỗ Mai I", "domaii@gmail.com", "Nhà tiếp thị kỹ thuật số và sáng tạo nội dung.", "https://i.pravatar.cc/300?img=9", "Thời trang, Viết blog, Nhiếp ảnh"));
    defaultUsers.add(new UserProfile(10, "Ngo Thanh J", "ngothanhj@gmail.com", "Kỹ sư DevOps yêu thích tự động hóa và CI/CD.", "https://i.pravatar.cc/300?img=10", "Chơi game, Cafe, Âm nhạc"));

    data = new UserList(defaultUsers);
    activity.runOnUiThread(() -> {
      UserAdapter adapter = new UserAdapter(data.getUsers(), context);
      gridview.setAdapter(adapter);
    });
  }

  public String readText(File file) {
    if (file == null) return "";
    try (InputStream stream = new FileInputStream(file);
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      StringBuilder buffer = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        buffer.append(line).append("\n");
      }
      return buffer.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }
}