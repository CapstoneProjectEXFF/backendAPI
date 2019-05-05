package com.capstone.exff.seeders;

import com.capstone.exff.constants.ExffRole;
import com.capstone.exff.entities.CategoryEntity;
import com.capstone.exff.entities.RoleEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.CategoryRepository;
import com.capstone.exff.repositories.RoleRepository;
import com.capstone.exff.repositories.UserRepository;
import com.capstone.exff.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class DatabaseSeeder {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private UserServices userServices;

    @Autowired
    public DatabaseSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            UserServices userServices)
    {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.userServices = userServices;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        try {
            seedRole();
            seedCategory();
            seedUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seedRole() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        if (roleEntities == null || roleEntities.isEmpty()) {
            RoleEntity role = new RoleEntity();
            role.setName(ExffRole.ROLE_USER);
            roleRepository.save(role);
            role = new RoleEntity();
            role.setName(ExffRole.ROLE_ADMIN);
            roleRepository.save(role);
        }
    }

    private void seedCategory() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        if (categoryEntities == null || categoryEntities.isEmpty()) {
            HashMap<String,ArrayList> superCategory = new HashMap<>();
            ArrayList<String> categories = new ArrayList<>();
            categories.add("Điện thoại");
            categories.add("Máy tính");
            categories.add("Máy tính bảng");
            categories.add("Máy ảnh");
            categories.add("Tivi");
            superCategory.put("Thiết bị điện tử", categories);

            categories = new ArrayList<>();
            categories.add("Chuột máy tính");
            categories.add("Bàn phím");
            categories.add("Tai nghe");
            categories.add("Loa");
            categories.add("Miếng lót chuột");
            superCategory.put("Phụ kiện - thiết bị số", categories);

            categories = new ArrayList<>();
            categories.add("Bàn ghế");
            categories.add("Đèn trang trí");
            categories.add("Tủ, kệ");
            categories.add("Lọ & Bình trang trí");
            superCategory.put("Đồ Gia Dụng", categories);

            categories = new ArrayList<>();
            categories.add("Đồ chơi");
            categories.add("Ghế ngồi ăn");
            categories.add("Ghế ngồi ô tô");
            categories.add("Nôi, Xe đẩy, Xe tập đi");
            categories.add("Địu, Dây đai an toàn");
            categories.add("Quần áo bé trai");
            categories.add("Quần áo bé gái");
            categories.add("Giày dép em bé");
            superCategory.put("Hàng Mẹ, Bé & Đồ Chơi", categories);

            categories = new ArrayList<>();
            categories.add("Bàn học");
            categories.add("Dụng cụ học tập");
            superCategory.put("Học tập", categories);

            categories = new ArrayList<>();
            categories.add("Quần áo nữ");
            categories.add("Quần áo nam");
            categories.add("Giày, dép");
            categories.add("Balo, cặp sách");
            categories.add("Phụ kiện cá nhân");
            superCategory.put("Thời trang", categories);

            categories = new ArrayList<>();
            categories.add("Sách giáo khoa");
            categories.add("Sách ngoại ngữ");
            categories.add("Sách văn học");
            categories.add("Sách chuyên ngành");
            categories.add("Truyện tranh");
            categories.add("Tạp chí");
            categories.add("Dụng cụ học tập");
            categories.add("Băng đĩa nhạc");
            categories.add("Poster, Fan-goods");
            superCategory.put("Sách Báo, Văn Phòng Phẩm, Băng Đĩa", categories);
            superCategory.forEach((s, arr) -> {
                CategoryEntity superCategoryEntity = new CategoryEntity();
                superCategoryEntity.setName(s);
                superCategoryEntity = categoryRepository.save(superCategoryEntity);
                int superCategoryId = superCategoryEntity.getId();
                ArrayList<CategoryEntity> cateEntities = new ArrayList<>();
                for (Object name : arr) {
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setName((String) name);
                    categoryEntity.setSupercategoryId(superCategoryId);
                    cateEntities.add(categoryEntity);
                }
                categoryRepository.saveAll(cateEntities);
            });
        }
    }

    private void seedUser() {
        String adminAccount = "0010000100";
        UserEntity userEntity = userRepository.findTop1ByPhoneNumber("0010000100");
        if (userEntity == null) {
            RoleEntity roleEntity = roleRepository.findTop1ByName(ExffRole.ROLE_ADMIN);
            String adminPassword = "0010000100";
            userServices.register(adminAccount,adminPassword,"Exff", "Công viên phần mềm Quang Trung", roleEntity);
        }
    }
}
