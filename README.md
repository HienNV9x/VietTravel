1. Trong Project Repository em có đính kèm thêm 1 Folder Database "viettravel_db". A/C có thể copy Folder này vào Hệ Quản trị cơ sở dữ liệu hiện có để có dữ liệu chạy Project VietTravel.
2. Trong Project có sử dụng Phương pháp OAuth2 Google nên khi Push GitHub báo lỗi. Em đã loại bỏ phần config OAuth2 trong Project để có thể Push GitHub.
   A/C có thể bổ sung lại phần config OAUth2 trong File application.properties như sau:
   #Feature OAuth2 Google
      spring.security.oauth2.client.registration.google.client-id=700767894663-tvc290j7thje0avo3ee3qpmfirah1tqu.apps.googleusercontent.com
      spring.security.oauth2.client.registration.google.client-secret=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
      spring.security.oauth2.client.registration.google.scope=profile,email
