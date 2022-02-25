package shop.helloshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.helloshop.domain.entity.UploadFile;
import shop.helloshop.domain.entity.items.Clothes;
import shop.helloshop.domain.entity.items.Item;
import shop.helloshop.domain.entity.items.Phone;
import shop.helloshop.domain.service.ItemService;
import shop.helloshop.domain.service.MemberService;
import shop.helloshop.web.FileChange;
import shop.helloshop.web.argumentresolver.Login;
import shop.helloshop.web.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final FileChange fileChange;




    @GetMapping("/item/select")
    public String selectForm(Model model) {
        ItemForm itemForm = new ItemForm();
        model.addAttribute("form", itemForm);
        return "item/selectItem";
    }

    @PostMapping("/item/select")
    public String selectItem(@RequestParam String select, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("item",select);
        return "redirect:/item/add/{item}";
    }

    @GetMapping("/item/add/{item}")
    public String itemAddForm(Model model, @PathVariable("item") String selectItem) {

        if (!selectItem.equals("T") && !selectItem.equals("P")) {
            return "redirect:/item/select";
        }

        ItemForm itemForm = new ItemForm();
        itemForm.setSelect(selectItem);

        model.addAttribute("form", itemForm);
        return "/item/addItem";
    }

    @PostMapping("/item/add/{item}")
    public String itemAdd(@Validated @ModelAttribute("form") ItemForm itemForm, BindingResult bindingResult, @PathVariable("item") String selectItem,
                          @Login MemberSessionDto memberSession,RedirectAttributes redirectAttributes) throws IOException {

        itemForm.setSelect(selectItem);

        if (bindingResult.hasErrors()) {
            log.info("{}",bindingResult.getFieldErrors());
            return "/item/addItem";
        }

        List<UploadFile> uploadFiles = fileChange.storeFiles(itemForm.getMultipartFileList());

        Item item = createItem(itemForm);

        for (UploadFile uploadFile : uploadFiles) {
            item.addUploadFile(uploadFile);
        }

        itemService.save(item,memberSession.getId());

        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/item/view/{itemId}";
    }

    @GetMapping("/item/update/{itemId}")
    public String updateForm(@PathVariable Long itemId,Model model) {

        ItemForm updateForm = createUpdateForm(itemId);
        model.addAttribute("form",updateForm);

        return "/item/updateItem";
    }

    @PostMapping("/item/update/{itemId}")
    public String updateItem(@Validated @ModelAttribute("form") ItemForm itemForm,BindingResult bindingResult
            ,@PathVariable Long itemId,RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()){
            return "/item/updateItem";
        }

        List<UploadFile> uploadFiles = fileChange.storeFiles(itemForm.getMultipartFileList());

        itemUpdateMethod(itemForm,itemId,uploadFiles);

        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/item/view/{itemId}";
    }

    @GetMapping("item/delete/{itemId}")
    public String deleteItem(@PathVariable Long itemId) {
        itemService.remove(itemId);
        return "redirect:/";
    }


    @GetMapping("/item/view/{itemId}")
    public String itemView(@PathVariable Long itemId, Model model, @Login MemberSessionDto sessionDto) {


        if (itemId == null) {
            return "redirect:/";
        }

        ItemViewForm viewForm = createViewForm(itemId);

        if (viewForm == null) {
            return "redirect:/";
        }

        if (sessionDto != null) {
            Item findItem = itemService.findOne(itemId);
            if (findItem.getMember().getId() == sessionDto.getId()) {
                model.addAttribute("check", itemId);
            }
            model.addAttribute("member", sessionDto);
        }


        model.addAttribute("item", viewForm);

        return "/item/itemView";
    }

    @PostMapping("/item/view/{itemId}")
    public String shopCartSession(HttpServletRequest request, RedirectAttributes redirectAttributes,
                                  @PathVariable Long itemId, @RequestParam(defaultValue = "0") int count,
                                  @Login MemberSessionDto memberSessionDto) {

        redirectAttributes.addAttribute("id",itemId);

        if(count == 0){
            return "redirect:/item/view/{id}";
        }

        if (memberSessionDto == null){
            return "redirect:/login";
        }

        ShopCartSession shopCartSession = new ShopCartSession();
        shopCartSession.setId(itemId);
        shopCartSession.setCount(count);

        HttpSession session = request.getSession(false);
        Object list = session.getAttribute(SessionKey.CART_SESSION);

        if(list == null){
            List<ShopCartSession> sessionList = new ArrayList<>();
            sessionList.add(shopCartSession);
            session.setAttribute(SessionKey.CART_SESSION,sessionList);
        }

        if (list != null) {
            List<ShopCartSession> orderItemList = (List<ShopCartSession>) list;
            orderItemList.add(shopCartSession);
            session.setAttribute(SessionKey.CART_SESSION,list);
        }

        return "redirect:/item/view/{id}";
    }


    @GetMapping("/item/list/{page}")
    public String pageView(@PathVariable Integer page , @RequestParam(defaultValue = "P") String sort,
                           Model model , @Login MemberSessionDto memberSessionDto) {

        if (page == null || sort == null || (!sort.equals("P") && !sort.equals("R"))) {
            return "redirect:/";
        }

        String selectSort = null;

        if (sort.equals("P")){
            selectSort = FindSort.Item_Popularity_List;
        }

        if (sort.equals("R")) {
            selectSort = FindSort.Item_Recent_List;
        }

        List<Item> itemList = itemService.findList(selectSort, page);

        if (itemList == null) {
            return "redirect:/";
        }

        List<ItemViewForm> itemViews = new ArrayList<>();

        for (Item item : itemList) {
            ItemViewForm view = ItemViewForm.createViewHome(item.getId(), item.getName(), item.getPrice(), item.getUploadFiles());
            itemViews.add(view);
        }

        int[] count = findCount();

        model.addAttribute("sort", sort);
        model.addAttribute("count", count);
        model.addAttribute("items", itemViews);
        model.addAttribute("member", memberSessionDto);

        return "/item/itemList";
    }



    @ResponseBody
    @GetMapping("/img/{filename}")
    public Resource viewImg(@PathVariable String filename) throws MalformedURLException {

        return new UrlResource("file:" + fileChange.getFullPath(filename));
    }



    private void itemUpdateMethod(ItemForm itemForm,Long itemId ,List<UploadFile> uploadFiles) {

        Item item = itemService.itemViewForm(itemId);

        if (itemForm.getSelect().equals("P")) {
            Phone phone = new Phone();
            phone.setQuantity(itemForm.getQuantity());
            phone.setPrice(itemForm.getPrice());
            phone.setName(itemForm.getName());
            phone.setPhoneColor(itemForm.getPhoneColor());
            itemService.updatePhone(itemId,phone,uploadFiles);
            return;
        }

        Clothes clothes = new Clothes();
        clothes.setQuantity(itemForm.getQuantity());
        clothes.setPrice(itemForm.getPrice());
        clothes.setName(itemForm.getName());
        clothes.setItemSize(itemForm.getItemSize());
        itemService.updateClothes(itemId,clothes,uploadFiles);

    }

    private int[] findCount() {

        int count = itemService.findCount().intValue();
        int pageNumber = count/16;

        if(count % 16 != 0){
            pageNumber += 1;
        }

        int[] findArr =new int[pageNumber];

        for(int i = 0 ; i < pageNumber ; i++){
            findArr[i] = i+1;
        }

        return findArr;
    }

    private Item createItem(ItemForm itemForm) {

        if(itemForm.getSelect().equals("T")){
            Clothes clothes = new Clothes();
            clothes.setItemSize(itemForm.getItemSize());
            clothes.setName(itemForm.getName());
            clothes.setPrice(itemForm.getPrice());
            clothes.setQuantity(itemForm.getQuantity());
            return clothes;
        } else if (itemForm.getSelect().equals("P")) {
            Phone phone = new Phone();
            phone.setName(itemForm.getName());
            phone.setQuantity(itemForm.getQuantity());
            phone.setPhoneColor(itemForm.getPhoneColor());
            phone.setPrice(itemForm.getPrice());
            return phone;
        }
        return null;
    }

    private ItemViewForm createViewForm(Long itemId) {

            Item item = itemService.itemViewForm(itemId);

            if(item.getType().equals("P")) {
                Phone phone = (Phone) item;
                return ItemViewForm.createPhoneForm(phone.getId(), phone.getName(), phone.getPrice(),
                        phone.getQuantity(), phone.getSalesQuantity(), phone.getPhoneColor(), phone.getUploadFiles());
            }

            if (item.getType().equals("T")) {
                Clothes clothes = (Clothes) item;
                return ItemViewForm.createClothesForm(clothes.getId(),clothes.getName(), clothes.getPrice(),
                        clothes.getQuantity(), clothes.getSalesQuantity(), clothes.getItemSize(), clothes.getUploadFiles());

        }

        return null;
    }

    private ItemForm createUpdateForm(Long itemId) {

        Item item = itemService.itemViewForm(itemId);

        if(item.getType().equals("P")){
            Phone phone = (Phone) item;
            return ItemForm.phoneUpdateForm(phone);
        }

        if (item.getType().equals("T")) {
            Clothes clothes = (Clothes) item;
            return ItemForm.clothesUpdateForm(clothes);
        }

        return null;

    }
}
