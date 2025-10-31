package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.DishTemplate;
import gr1.fpt.bambikitchen.model.IngredientCategory;
import gr1.fpt.bambikitchen.model.Nutrition;
import gr1.fpt.bambikitchen.model.dto.request.AccountCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.IngredientCreateRequest;
import gr1.fpt.bambikitchen.model.enums.DishType;
import gr1.fpt.bambikitchen.model.enums.SizeCode;
import gr1.fpt.bambikitchen.model.enums.Unit;
import gr1.fpt.bambikitchen.repository.*;
import gr1.fpt.bambikitchen.service.AccountService;
import gr1.fpt.bambikitchen.service.impl.DishService;
import gr1.fpt.bambikitchen.service.IngredientService;
import gr1.fpt.bambikitchen.service.impl.DishTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
public class DumpDataController {

    @Autowired
    IngredientService ingredientService;
    @Autowired
    DishTemplateService dishTemplateService;
    @Autowired
    DishService dishService;
    @Autowired
    AccountService accountService;
    @Autowired
    IngredientCategoryRepository ingredientCategoryRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DishTemplateRepository dishTemplateRepository;
    @Autowired
    DishRepository dishRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    NutritionRepository nutritionRepository;

    @Operation(summary = "IMPORTANCE", description = "CÔNG NGHỆ LÕI CỦA BÀN HỒNG, CHỈ DÙNG KHI CẤP BÁCH ")

    @GetMapping("/dump-data")
    public String dumpData(@RequestParam String password) throws IOException, InterruptedException {
        if (!password.equals("bambi2025")) {
            throw new CustomException( "Ai cho mà Dump", HttpStatus.BANDWIDTH_LIMIT_EXCEEDED );
        }
        dumpIngreCate();
        ingredientCategoryRepository.flush();
        dumpAccount();
        accountRepository.flush();
        dumpIngre();
        ingredientRepository.flush();
        dumpDishTemplate();
        dishTemplateRepository.flush();
        //dumpDish();
        dishRepository.flush();
        dumpNutrition();

        return "Dump data successfully!";
    }



    public void dumpIngreCate() {
        List<IngredientCategory> categories = new ArrayList<>();
        categories.add(new IngredientCategory("Hải sản", "Ngon"));
        categories.add(new IngredientCategory("Rau và Cỏ", "Hành Tinh Bò"));
        categories.add(new IngredientCategory("Thịt", "G O A T"));
        categories.add(new IngredientCategory("Tinh Bột", "No"));
        categories.add(new IngredientCategory("Trái cây", "Cung cấp vitamin"));
        categories.add(new IngredientCategory("Hạt & Đậu", "Giàu chất xơ và béo tốt"));
        categories.add(new IngredientCategory("Nấm", "Tăng hương vị umami"));
        categories.add(new IngredientCategory("Nước", "Chất lỏng"));
        ingredientCategoryRepository.saveAll( categories );
    }

    public void dumpAccount(){
        accountService.save(new AccountCreateRequest() {{
            setName("Khang");
            setPassword("12345678");
            setMail("wtfbro@gmail.com");
        }});
    }

    public void dumpIngre() throws IOException {

        // Tạo 1 file dummy dùng chung
        MultipartFile dummyFile = new MockMultipartFile(
                "file",
                "ingredient.jpg",
                "image/jpeg",
                new byte[0]
        );


        ingredientService.save(new IngredientCreateRequest() {{
            setName("Tôm");
            setCategoryId(1);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Mực");
            setCategoryId(1);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Bò");
            setCategoryId(3);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Gà");
            setCategoryId(3);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Cá hồi");
            setCategoryId(1);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Cá ngừ");
            setCategoryId(1);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Cải thìa");
            setCategoryId(2);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Cải bó xôi");
            setCategoryId(2);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Cải ngọt");
            setCategoryId(2);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Cải thảo");
            setCategoryId(2);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Xà lách");
            setCategoryId(2);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Cà chua");
            setCategoryId(5);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Dưa leo");
            setCategoryId(5);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Khoai tây");
            setCategoryId(4);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Cơm");
            setCategoryId(4);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Mì");
            setCategoryId(4);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Đậu Hà Lan");
            setCategoryId(6);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Đậu Đen");
            setCategoryId(6);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Nấm Kim Châm");
            setCategoryId(7);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Nấm Hương");
            setCategoryId(7);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Nấm Bào Ngư");
            setCategoryId(7);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Nấm Rơm");
            setCategoryId(7);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Nước Lọc");
            setCategoryId(8);
            setUnit(Unit.LITER);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save (new IngredientCreateRequest() {{
            setName("Nước Dừa");
            setCategoryId(8);
            setUnit(Unit.LITER);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save( new IngredientCreateRequest() {{
            setName("Thịt Heo");
            setCategoryId(3);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Thịt Cừu");
            setCategoryId(3);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Thịt Vịt");
            setCategoryId(3);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Bí Đỏ");
            setCategoryId(5);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Bí Xanh");
            setCategoryId(5);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Táo");
            setCategoryId(5);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Chuối");
            setCategoryId(5);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Cam");
            setCategoryId(5);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Quýt");
            setCategoryId(5);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Hạt Chia");
            setCategoryId(6);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Hạt Hướng Dương");
            setCategoryId(6);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Hạt Óc Chó");
            setCategoryId(6);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Hạt Hạnh Nhân");
            setCategoryId(6);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Hạt Điều");
            setCategoryId(6);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Hạt Dẻ");
            setCategoryId(6);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
        ingredientService.save(new IngredientCreateRequest() {{
            setName("Hạt Đậu Phộng");
            setCategoryId(6);
            setUnit(Unit.GRAM);
            setQuantity(10000);
            setFile( dummyFile);
        }});
    }

    public void dumpDishTemplate ()
    {
        dishTemplateService.saveDishTemplate( new DishTemplate(SizeCode.S,"Tô Chuột",0.7,0.7, 2,2,2) );
        dishTemplateService.saveDishTemplate( new DishTemplate(SizeCode.M,"Tô Người",1.0,1.0, 2,4,3) );
        dishTemplateService.saveDishTemplate( new DishTemplate(SizeCode.L,"Tô Khủng Long",1.3,1.3, 3,6,4) );
    }

    public void dumpDish() {
        MultipartFile dummyFile = new MockMultipartFile(
                "file",
                "ingredient.jpg",
                "image/jpeg",
                new byte[0]
        );
        // Món ăn ví dụ ban đầu
        dishService.save(new DishCreateRequest() {{
            setName("Cơm Mực ăn kèm cà chua");
            setDescription("Phá Hủy Làng Chài");
            setPrice(59000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(2, 200); // Mực
                put(15, 200); // Cơm
                put(12, 100); // Cà chua
            }});
        }});

        // Món 1: Mì Bò Xào Rau Cải
        dishService.save(new DishCreateRequest() {{
            setName("Mì Bò Xào Rau Cải");
            setDescription("Mì dai giòn kết hợp bò mềm và rau cải tươi");
            setPrice(65000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(3, 150); // Bò
                put(16, 200); // Mì
                put(7, 100); // Cải thìa
            }});
        }});

        // Món 2: Cơm Gà Nướng Dưa Leo
        dishService.save(new DishCreateRequest() {{
            setName("Cơm Gà Nướng Dưa Leo");
            setDescription("Cơm dẻo với gà nướng thơm lừng và dưa leo giòn");
            setPrice(55000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(4, 150); // Gà
                put(15, 200); // Cơm
                put(13, 100); // Dưa leo
            }});
        }});

        // Món 3: Tôm Rim Nấm Hương
        dishService.save(new DishCreateRequest() {{
            setName("Tôm Rim Nấm Hương");
            setDescription("Tôm tươi rim đậm đà với nấm hương thơm nồng");
            setPrice(70000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(1, 200); // Tôm
                put(20, 100); // Nấm Hương
            }});
        }});

        // Món 4: Cá Hồi Áp Chảo Xà Lách
        dishService.save(new DishCreateRequest() {{
            setName("Cá Hồi Áp Chảo Xà Lách");
            setDescription("Cá hồi áp chảo vàng ươm ăn kèm xà lách tươi");
            setPrice(85000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(5, 150); // Cá hồi
                put(11, 100); // Xà lách
            }});
        }});

        // Món 5: Thịt Heo Kho Bí Đỏ
        dishService.save(new DishCreateRequest() {{
            setName("Thịt Heo Kho Bí Đỏ");
            setDescription("Thịt heo kho mềm béo với bí đỏ ngọt tự nhiên");
            setPrice(60000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(25, 200); // Thịt Heo
                put(28, 150); // Bí Đỏ
            }});
        }});

        // Món 6: Mì Trộn Đậu Hà Lan và Nấm Kim Châm
        dishService.save(new DishCreateRequest() {{
            setName("Mì Trộn Đậu Hà Lan và Nấm Kim Châm");
            setDescription("Mì trộn hấp dẫn với đậu Hà Lan và nấm kim châm giòn");
            setPrice(52000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(16, 200); // Mì
                put(17, 100); // Đậu Hà Lan
                put(19, 100); // Nấm Kim Châm
            }});
        }});

        // Món 7: Cơm Chiên Hải Sản
        dishService.save(new DishCreateRequest() {{
            setName("Cơm Chiên Hải Sản");
            setDescription("Cơm chiên thơm ngon với tôm và mực tươi");
            setPrice(68000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(1, 100); // Tôm
                put(2, 100); // Mực
                put(15, 200); // Cơm
            }});
        }});

        // Món 8: Gà Chiên Nước Dừa
        dishService.save(new DishCreateRequest() {{
            setName("Gà Chiên Nước Dừa");
            setDescription("Gà chiên giòn tan với hương vị nước dừa đặc trưng");
            setPrice(62000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(4, 200); // Gà
                put(24, 300); // Nước Dừa
            }});
        }});

        // Món 9: Rau Cải Xào Nấm Bào Ngư
        dishService.save(new DishCreateRequest() {{
            setName("Rau Cải Xào Nấm Bào Ngư");
            setDescription("Rau cải ngọt xào cùng nấm bào ngư thơm ngon");
            setPrice(45000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(9, 150); // Cải ngọt
                put(21, 100); // Nấm Bào Ngư
            }});
        }});

        // Món 10: Cơm Tấm Sườn Nướng
        dishService.save(new DishCreateRequest() {{
            setName("Cơm Tấm Sườn Nướng");
            setDescription("Cơm tấm dẻo thơm với sườn nướng đậm đà");
            setPrice(60000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(25, 200); // Thịt Heo
                put(15, 200); // Cơm
            }});
        }});

        // Món 11: Mì Cá Ngừ Xào Cải Thảo
        dishService.save(new DishCreateRequest() {{
            setName("Mì Cá Ngừ Xào Cải Thảo");
            setDescription("Mì thơm ngon xào với cá ngừ và cải thảo giòn ngọt");
            setPrice(67000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(6, 150); // Cá ngừ
                put(16, 200); // Mì
                put(10, 100); // Cải thảo
            }});
        }});

        // Món 12: Cơm Thịt Cừu Nướng Bí Xanh
        dishService.save(new DishCreateRequest() {{
            setName("Cơm Thịt Cừu Nướng Bí Xanh");
            setDescription("Cơm dẻo với thịt cừu nướng thơm và bí xanh tươi");
            setPrice(75000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(26, 150); // Thịt Cừu
                put(15, 200); // Cơm
                put(29, 100); // Bí Xanh
            }});
        }});

        // Món 13: Tôm Xào Hạt Điều
        dishService.save(new DishCreateRequest() {{
            setName("Tôm Xào Hạt Điều");
            setDescription("Tôm tươi xào với hạt điều béo bùi");
            setPrice(72000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(1, 200); // Tôm
                put(38, 100); // Hạt Điều
            }});
        }});

        // Món 14: Gà Kho Nấm Rơm
        dishService.save(new DishCreateRequest() {{
            setName("Gà Kho Nấm Rơm");
            setDescription("Gà kho đậm đà với nấm rơm thơm ngon");
            setPrice(58000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(4, 200); // Gà
                put(22, 100); // Nấm Rơm
            }});
        }});

        // Món 15: Cơm Chiên Đậu Đen và Cà Chua
        dishService.save(new DishCreateRequest() {{
            setName("Cơm Chiên Đậu Đen và Cà Chua");
            setDescription("Cơm chiên với đậu đen bùi và cà chua tươi");
            setPrice(51000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(18, 100); // Đậu Đen
                put(15, 200); // Cơm
                put(12, 100); // Cà chua
            }});
        }});

        // Món 16: Thịt Vịt Nướng Xà Lách
        dishService.save(new DishCreateRequest() {{
            setName("Thịt Vịt Nướng Xà Lách");
            setDescription("Thịt vịt nướng thơm lừng ăn kèm xà lách giòn");
            setPrice(69000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(27, 200); // Thịt Vịt
                put(11, 100); // Xà Lách
            }});
        }});

        // Món 17: Mì Xào Khoai Tây và Nấm Kim Châm
        dishService.save(new DishCreateRequest() {{
            setName("Mì Xào Khoai Tây và Nấm Kim Châm");
            setDescription("Mì xào với khoai tây bùi và nấm kim châm giòn");
            setPrice(53000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(16, 200); // Mì
                put(14, 150); // Khoai Tây
                put(19, 100); // Nấm Kim Châm
            }});
        }});

        // Món 18: Bò Xào Hạt Hạnh Nhân
        dishService.save(new DishCreateRequest() {{
            setName("Bò Xào Hạt Hạnh Nhân");
            setDescription("Bò mềm xào với hạt hạnh nhân béo giòn");
            setPrice(74000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(3, 150); // Bò
                put(37, 100); // Hạt Hạnh Nhân
            }});
        }});

        // Món 19: Cơm Tấm Cá Hồi và Dưa Leo
        dishService.save(new DishCreateRequest() {{
            setName("Cơm Tấm Cá Hồi và Dưa Leo");
            setDescription("Cơm tấm với cá hồi áp chảo và dưa leo tươi mát");
            setPrice(82000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(5, 150); // Cá Hồi
                put(15, 200); // Cơm
                put(13, 100); // Dưa Leo
            }});
        }});

        // Món 20: Rau Cải Bó Xôi Xào Nấm Hương
        dishService.save(new DishCreateRequest() {{
            setName("Rau Cải Bó Xôi Xào Nấm Hương");
            setDescription("Cải bó xôi xào với nấm hương thơm ngon bổ dưỡng");
            setPrice(47000);
            setFile(dummyFile);
            setAccount(accountService.findById(1));
            setDishType(DishType.PRESET);
            setPublic(true);
            setIngredients(new HashMap<Integer, Integer>() {{
                put(8, 150); // Cải Bó Xôi
                put(20, 100); // Nấm Hương
            }});
        }});



    }

    public void dumpNutrition() {
        List<Nutrition> nutritions = new ArrayList<>();
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(1).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(99)
                .protein(20.1f)
                .carb(0.2f)
                .fiber(0.0f)
                .iron(2.4f)
                .sodium(119.0f)
                .calcium(10.0f)
                .sugar(0.0f)
                .sat_fat(0.5f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(2).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(92)
                .protein(15.6f)
                .carb(3.0f)
                .fiber(0.0f)
                .iron(0.8f)
                .sodium(45.0f)
                .calcium(4.0f)
                .sugar(0.0f)
                .sat_fat(0.3f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(3).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(250)
                .protein(26.0f)
                .carb(0.0f)
                .fiber(0.0f)
                .iron(2.6f)
                .sodium(72.0f)
                .calcium(11.0f)
                .sugar(0.0f)
                .sat_fat(5.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(4).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(165)
                .protein(23.2f)
                .carb(0.0f)
                .fiber(0.0f)
                .iron(1.0f)
                .sodium(82.0f)
                .calcium(12.0f)
                .sugar(0.0f)
                .sat_fat(3.5f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(5).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(206)
                .protein(20.0f)
                .carb(0.0f)
                .fiber(0.0f)
                .iron(0.8f)
                .sodium(61.0f)
                .calcium(9.0f)
                .sugar(0.0f)
                .sat_fat(13.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(6).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(130)
                .protein(29.0f)
                .carb(0.0f)
                .fiber(0.0f)
                .iron(1.3f)
                .sodium(90.0f)
                .calcium(6.0f)
                .sugar(0.0f)
                .sat_fat(1.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(7).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(23)
                .protein(2.0f)
                .carb(3.1f)
                .fiber(1.0f)
                .iron(0.6f)
                .sodium(33.0f)
                .calcium(81.0f)
                .sugar(0.7f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(8).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(23)
                .protein(2.9f)
                .carb(3.6f)
                .fiber(2.2f)
                .iron(2.7f)
                .sodium(79.0f)
                .calcium(99.0f)
                .sugar(0.9f)
                .sat_fat(0.1f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(9).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(13)
                .protein(1.3f)
                .carb(2.0f)
                .fiber(0.9f)
                .iron(0.2f)
                .sodium(21.0f)
                .calcium(47.0f)
                .sugar(0.8f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(10).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(16)
                .protein(1.5f)
                .carb(2.2f)
                .fiber(1.0f)
                .iron(0.3f)
                .sodium(25.0f)
                .calcium(51.0f)
                .sugar(0.9f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(11).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(15)
                .protein(1.4f)
                .carb(2.8f)
                .fiber(1.3f)
                .iron(0.4f)
                .sodium(28.0f)
                .calcium(36.0f)
                .sugar(1.0f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(12).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(18)
                .protein(0.9f)
                .carb(3.9f)
                .fiber(1.2f)
                .iron(0.3f)
                .sodium(5.0f)
                .calcium(10.0f)
                .sugar(2.6f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(13).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(15)
                .protein(0.7f)
                .carb(2.6f)
                .fiber(0.5f)
                .iron(0.2f)
                .sodium(2.0f)
                .calcium(16.0f)
                .sugar(1.7f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(14).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(77)
                .protein(2.0f)
                .carb(17.0f)
                .fiber(2.2f)
                .iron(0.8f)
                .sodium(10.0f)
                .calcium(12.0f)
                .sugar(0.8f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(15).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(130)
                .protein(2.7f)
                .carb(28.0f)
                .fiber(0.4f)
                .iron(0.2f)
                .sodium(1.0f)
                .calcium(10.0f)
                .sugar(0.1f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(16).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(131)
                .protein(5.0f)
                .carb(25.0f)
                .fiber(1.0f)
                .iron(0.5f)
                .sodium(5.0f)
                .calcium(15.0f)
                .sugar(0.3f)
                .sat_fat(0.1f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(17).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(81)
                .protein(5.4f)
                .carb(14.0f)
                .fiber(5.0f)
                .iron(1.5f)
                .sodium(6.0f)
                .calcium(36.0f)
                .sugar(5.7f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(18).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(132)
                .protein(8.9f)
                .carb(23.0f)
                .fiber(8.7f)
                .iron(2.1f)
                .sodium(2.0f)
                .calcium(27.0f)
                .sugar(0.5f)
                .sat_fat(0.5f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(19).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(26)
                .protein(2.1f)
                .carb(5.0f)
                .fiber(2.5f)
                .iron(0.4f)
                .sodium(5.0f)
                .calcium(2.0f)
                .sugar(0.5f)
                .sat_fat(0.1f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(20).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(28)
                .protein(2.6f)
                .carb(5.1f)
                .fiber(2.3f)
                .iron(0.5f)
                .sodium(6.0f)
                .calcium(3.0f)
                .sugar(0.6f)
                .sat_fat(0.2f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(21).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(34)
                .protein(3.1f)
                .carb(6.0f)
                .fiber(2.6f)
                .iron(0.6f)
                .sodium(7.0f)
                .calcium(4.0f)
                .sugar(0.7f)
                .sat_fat(0.2f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(22).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(22)
                .protein(2.0f)
                .carb(4.0f)
                .fiber(1.8f)
                .iron(0.3f)
                .sodium(4.0f)
                .calcium(2.0f)
                .sugar(0.4f)
                .sat_fat(0.1f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(23).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(0)
                .protein(0.0f)
                .carb(0.0f)
                .fiber(0.0f)
                .iron(0.0f)
                .sodium(0.0f)
                .calcium(0.0f)
                .sugar(0.0f)
                .sat_fat(0.0f)
                .per_unit("1000ml")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(24).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(19)
                .protein(0.2f)
                .carb(3.7f)
                .fiber(1.1f)
                .iron(0.1f)
                .sodium(12.0f)
                .calcium(24.0f)
                .sugar(2.6f)
                .sat_fat(0.0f)
                .per_unit("1000ml")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(25).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(242)
                .protein(27.0f)
                .carb(0.0f)
                .fiber(0.0f)
                .iron(1.1f)
                .sodium(89.0f)
                .calcium(8.0f)
                .sugar(0.0f)
                .sat_fat(9.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(26).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(294)
                .protein(25.0f)
                .carb(0.0f)
                .fiber(0.0f)
                .iron(1.8f)
                .sodium(100.0f)
                .calcium(17.0f)
                .sugar(0.0f)
                .sat_fat(15.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(27).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(179)
                .protein(19.0f)
                .carb(0.0f)
                .fiber(0.0f)
                .iron(1.6f)
                .sodium(86.0f)
                .calcium(11.0f)
                .sugar(0.0f)
                .sat_fat(4.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(28).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(26)
                .protein(1.0f)
                .carb(6.0f)
                .fiber(0.5f)
                .iron(0.3f)
                .sodium(4.0f)
                .calcium(21.0f)
                .sugar(2.8f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(29).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(21)
                .protein(0.8f)
                .carb(4.0f)
                .fiber(0.4f)
                .iron(0.2f)
                .sodium(2.0f)
                .calcium(19.0f)
                .sugar(2.0f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(30).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(52)
                .protein(0.3f)
                .carb(14.0f)
                .fiber(1.7f)
                .iron(0.1f)
                .sodium(1.0f)
                .calcium(6.0f)
                .sugar(10.0f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(31).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(90)
                .protein(1.1f)
                .carb(23.0f)
                .fiber(2.6f)
                .iron(0.3f)
                .sodium(1.0f)
                .calcium(5.0f)
                .sugar(17.0f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(32).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(47)
                .protein(0.9f)
                .carb(12.0f)
                .fiber(1.8f)
                .iron(0.1f)
                .sodium(1.0f)
                .calcium(40.0f)
                .sugar(9.0f)
                .sat_fat(0.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(33).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(486)
                .protein(16.5f)
                .carb(49.0f)
                .fiber(34.4f)
                .iron(7.7f)
                .sodium(16.0f)
                .calcium(631.0f)
                .sugar(0.0f)
                .sat_fat(5.8f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(34).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(584)
                .protein(20.5f)
                .carb(20.0f)
                .fiber(8.6f)
                .iron(5.2f)
                .sodium(9.0f)
                .calcium(267.0f)
                .sugar(2.4f)
                .sat_fat(9.3f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(35).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(654)
                .protein(15.2f)
                .carb(13.0f)
                .fiber(9.6f)
                .iron(3.1f)
                .sodium(10.0f)
                .calcium(98.0f)
                .sugar(7.0f)
                .sat_fat(49.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(36).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(607)
                .protein(21.2f)
                .carb(13.0f)
                .fiber(10.5f)
                .iron(4.9f)
                .sodium(7.0f)
                .calcium(273.0f)
                .sugar(4.0f)
                .sat_fat(49.2f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(37).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(579)
                .protein(20.0f)
                .carb(22.0f)
                .fiber(10.5f)
                .iron(5.5f)
                .sodium(5.0f)
                .calcium(248.0f)
                .sugar(4.8f)
                .sat_fat(37.0f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(38).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(553)
                .protein(18.2f)
                .carb(27.0f)
                .fiber(10.0f)
                .iron(5.3f)
                .sodium(12.0f)
                .calcium(278.0f)
                .sugar(5.2f)
                .sat_fat(43.2f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(39).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(620)
                .protein(17.1f)
                .carb(16.0f)
                .fiber(9.4f)
                .iron(4.7f)
                .sodium(6.0f)
                .calcium(226.0f)
                .sugar(3.9f)
                .sat_fat(45.3f)
                .per_unit("100g")
                .build());
        nutritions.add(Nutrition.builder()
                .ingredient(ingredientRepository.findById(40).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.NOT_FOUND)))
                .calories(567)
                .protein(25.8f)
                .carb(16.0f)
                .fiber(9.2f)
                .iron(4.2f)
                .sodium(5.0f)
                .calcium(241.0f)
                .sugar(4.5f)
                .sat_fat(45.0f)
                .per_unit("100g")
                .build());

        nutritionRepository.saveAll(nutritions);
    }


}

