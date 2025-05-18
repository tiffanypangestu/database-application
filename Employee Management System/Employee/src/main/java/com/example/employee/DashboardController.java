package com.example.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button beranda_button;

    @FXML
    private BarChart<?, ?> beranda_diagramKaryawan;

    @FXML
    private AnchorPane beranda_form;

    @FXML
    private Button close;

    @FXML
    private TextField gaji_GajiKaryawan;

    @FXML
    private TextField gaji_IDKaryawan;

    @FXML
    private Button gaji_button;

    @FXML
    private Button gaji_clearbutton;

    @FXML
    private AnchorPane gaji_form;

    @FXML
    private TableColumn<DataKaryawan, String> gaji_kolom_GajiKaryawan;

    @FXML
    private TableColumn<DataKaryawan, String> gaji_kolom_IDKaryawan;

    @FXML
    private TableColumn<DataKaryawan, String> gaji_kolom_NamaLengkap;

    @FXML
    private TableColumn<DataKaryawan, String> gaji_kolom_PosisiKerja;

    @FXML
    private Label gaji_namalengkap;

    @FXML
    private Label gaji_posisikerja;

    @FXML
    private TableView<DataKaryawan> gaji_tableView;

    @FXML
    private Button gaji_updatebutton;

    @FXML
    private Label home_jumlahkaryawan;

    @FXML
    private Label home_karyawannonaktif;

    @FXML
    private Label home_totalkehadiran;

    @FXML
    private Button logout;

    @FXML
    private Button minimize;

    @FXML
    private Button tambahkaryawan_AddButton;

    @FXML
    private TextField tambahkaryawan_IDKaryawan;

    @FXML
    private TextField tambahkaryawan_NamaLengkap;

    @FXML
    private Button tambahkaryawan_PerbaruiButton;

    @FXML
    private Button tambahkaryawan_button;

    @FXML
    private Button tambahkaryawan_clearButton;

    @FXML
    private AnchorPane tambahkaryawan_form;

    @FXML
    private ImageView tambahkaryawan_gambar;

    @FXML
    private Button tambahkaryawan_hapusButton;

    @FXML
    private Button tambahkaryawan_importButton;

    @FXML
    private ComboBox<?> tambahkaryawan_jenisKelamin;

    @FXML
    private TableColumn<DataKaryawan, String> tambahkaryawan_kolom_IDKaryawan;

    @FXML
    private TableColumn<DataKaryawan, String> tambahkaryawan_kolom_JenisKelamin;

    @FXML
    private TableColumn<DataKaryawan, String> tambahkaryawan_kolom_NamaLengkap;

    @FXML
    private TableColumn<DataKaryawan, String> tambahkaryawan_kolom_NomorTelp;

    @FXML
    private TableColumn<DataKaryawan, String> tambahkaryawan_kolom_PosisiKerja;

    @FXML
    private TableColumn<DataKaryawan, String> tambahkaryawan_kolom_TanggalMasuk;

    @FXML
    private TextField tambahkaryawan_noTelp;

    @FXML
    private TextField tambahkaryawan_pencarian;

    @FXML
    private ComboBox<?> tambahkaryawan_posisiKerja;

    @FXML
    private TableView<DataKaryawan> tambahkaryawan_tableView;

    @FXML
    private Label username;

    private Connection connection;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    private Image gambar;

    public void totalKaryawanHome() {
        String sql = "SELECT COUNT(id) FROM datakaryawan";
        connection = Database.connectionDatabase();
        int hitung = 0;
        try {
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();
            while(result.next()) {
                hitung = result.getInt("COUNT(id)");
            }
            home_jumlahkaryawan.setText(String.valueOf(hitung));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void totalKehadiranHome() {
        String sql = "SELECT COUNT(id) FROM id_karyawan";

        connection = Database.connectionDatabase();
        int berhitung = 0;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()){
                berhitung = result.getInt("COUNT(id)");
            }
            home_totalkehadiran.setText(String.valueOf(berhitung));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void karyawanNonAktifHome() {
        String sql = "SELECT * FROM informasi_karyawan WHERE gaji != '0.0'";

        connection = Database.connectionDatabase();
        int hitung = 0;
        try {
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();

            while(result.next()) {
                hitung = result.getInt("COUNT(id)");
            }
            home_karyawannonaktif.setText(String.valueOf(hitung));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Diagram() {
        beranda_diagramKaryawan.getData().clear();

        String sql = "SELECT tanggal, COUNT(id) FROM datakaryawan GROUP BY tanggal ORDER BY TIMESTAMP(tanggal) ASC LIMIT 7";

        connection = Database.connectionDatabase();

        try {
            XYChart.Series diagram = new XYChart.Series();
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                diagram.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }
            beranda_diagramKaryawan.getData().add(diagram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void tambahKaryawanDataTambahan() {
        Date tanggal = new Date();
        java.sql.Date tanggalDatabase = new java.sql.Date(tanggal.getTime());
        String sql = "INSERT INTO datakaryawan " +
                "(id_karyawan,nama_lengkap,jenis_kelamin,nomor_telepon,posisi_kerja,gambar,tanggal)";

        connection = Database.connectionDatabase();

        try {
            Alert alert;
            if(tambahkaryawan_IDKaryawan.getText().isEmpty()
                    || tambahkaryawan_NamaLengkap.getText().isEmpty()
                    || tambahkaryawan_noTelp.getText().isEmpty()
                    || tambahkaryawan_jenisKelamin.getSelectionModel().getSelectedItem() == null
                    || tambahkaryawan_posisiKerja.getSelectionModel().getSelectedItem() == null
                    || DataFetch.path == null || DataFetch.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pesan salah!");
                alert.setHeaderText(null);
                alert.setContentText("Tolong masukkan kolom yang kosong!");
                alert.showAndWait();
            }
            else {
                String periksa = "SELECT id_karyawan FROM employee WHERE id_karyawan = '"
                        + tambahkaryawan_IDKaryawan.getText() + "'";

                statement = connection.createStatement();
                result = statement.executeQuery(periksa);

                if(result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Pesan salah!");
                    alert.setHeaderText(null);
                    alert.setContentText("ID Karyawan " + tambahkaryawan_IDKaryawan.getText() +
                            " sudah ada.");
                    alert.showAndWait();
                }

                else {
                    prepare = connection.prepareStatement(sql);
                    prepare.setString(1, tambahkaryawan_IDKaryawan.getText());
                    prepare.setString(2, tambahkaryawan_NamaLengkap.getText());
                    prepare.setString(3, tambahkaryawan_noTelp.getText());
                    prepare.setString(4, (String) tambahkaryawan_jenisKelamin.getSelectionModel().getSelectedItem());
                    prepare.setString(5, (String) tambahkaryawan_posisiKerja.getSelectionModel().getSelectedItem());

                    String url = DataFetch.path;
                    url = url.replace("\\", "\\\\");

                    prepare.setString(6, url);
                    prepare.setString(7, String.valueOf(tanggalDatabase));
                    prepare.executeUpdate();

                    String input = "INSERT INTO informasi_karyawan "
                            + "(id_karyawan, nama_lengkap, posisi_kerja, gaji, tanggal)"
                            + "VALUES(?,?,?,?,?)";

                    prepare = connection.prepareStatement(input);
                    prepare.setString(1, tambahkaryawan_IDKaryawan.getText());
                    prepare.setString(2, tambahkaryawan_NamaLengkap.getText());
                    prepare.setString(3, (String) tambahkaryawan_posisiKerja.getSelectionModel().getSelectedItem());
                    prepare.setString(4, "0.0");
                    prepare.setString(5, String.valueOf(tanggalDatabase));
                    prepare.executeUpdate();


                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Pesan Informasi: ");
                    alert.setHeaderText(null);
                    alert.setContentText("Berhasil ditambahkan!");
                    alert.showAndWait();

                    tampilkanDaftarKaryawanTambahan();
                    muatUlang();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hapus() {
        String sql = "DELETE FROM karyawan WHERE id_karyawan = '" +
                tambahkaryawan_IDKaryawan.getText()+"'";
        connection = Database.connectionDatabase();

        try {
            Alert alert;
            if(tambahkaryawan_IDKaryawan.getText().isEmpty()
                    || tambahkaryawan_NamaLengkap.getText().isEmpty()
                    || tambahkaryawan_noTelp.getText().isEmpty()
                    || tambahkaryawan_jenisKelamin.getSelectionModel().getSelectedItem() == null
                    || tambahkaryawan_posisiKerja.getSelectionModel().getSelectedItem() == null
                    || DataFetch.path == null || DataFetch.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pesan salah!");
                alert.setHeaderText(null);
                alert.setContentText("Tolong masukkan kolom yang kosong!");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Pesan Konfirmasi");
                alert.setHeaderText(null);
                alert.setContentText("Yakin untuk hapus ID Karyawan? " +
                        tambahkaryawan_IDKaryawan.getText()+"?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connection.createStatement();
                    statement.executeUpdate(sql);

                    String deleteInfo = "DELETE FROM employee_info WHERE employee_id = '"
                            + tambahkaryawan_IDKaryawan.getText() + "'";

                    prepare = connection.prepareStatement(deleteInfo);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Pesan Informasi");
                    alert.setHeaderText(null);
                    alert.setContentText("Data berhasil dihapus!");
                    alert.showAndWait();

                    tampilkanDaftarKaryawanTambahan();
                    muatUlang();
                }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void muatUlang() {
        tambahkaryawan_IDKaryawan.setText("");
        tambahkaryawan_NamaLengkap.setText("");
        tambahkaryawan_noTelp.setText("");
        tambahkaryawan_jenisKelamin.getSelectionModel().clearSelection();
        tambahkaryawan_posisiKerja.getSelectionModel().clearSelection();
        tambahkaryawan_gambar.setImage(null);
        DataFetch.path = "";
    }

    public void tambahkaryawanImportGambar() {
        FileChooser buka = new FileChooser();
        File file = buka.showOpenDialog(main_form.getScene().getWindow());

        if(file != null) {
            DataFetch.path = file.getAbsolutePath();
            gambar = new Image(file.toURI().toString(), 113, 150, false, true);
        }
    }

    private String [] list_posisikerja = {
            "UI/UX Designer",
            "IOS Developer",
            "Android Developer",
            "Mobile Web Developer",
            "Business Analyst",
            "Data Analytics",
            "Forest Trader",
            "Ethical Hacker",
            "Database Administrator",
            "Information System Auditor"
    };

    public void tambahPosisiKaryawan() {
        List<String> daftarposisi = new ArrayList<>();
        for (String data: list_posisikerja) {
            daftarposisi.add(data);
        }

        ObservableList daftar = FXCollections.observableArrayList(daftarposisi);
        tambahkaryawan_posisiKerja.setItems(daftar);
    }

    private String [] listjenisKelamin = {
            "Laki-Laki",
            "Perempuan"
    };

    public void tambahJenisKelamin() {
        List<String> daftarjeniskelamin = new ArrayList<>();

        for (String data: listjenisKelamin) {
            daftarjeniskelamin.add(data);
        }

        ObservableList datajeniskelamin = FXCollections.observableArrayList(daftarjeniskelamin);
        tambahkaryawan_jenisKelamin.setItems(datajeniskelamin);
    }

    public void perbaruiDataKaryawan() {
        String url = DataFetch.path;
        url = url.replace("\\", "\\\\");

        Date tanggal = new Date();
        java.sql.Date tanggalDatabase = new java.sql.Date(tanggal.getTime());
        String sql = "UPDATE employee SET nama_lengkap = '" +
                tambahkaryawan_NamaLengkap.getText() + "' jenis_kelamin = '" +
                tambahkaryawan_jenisKelamin.getSelectionModel().getSelectedItem()+"', nomor_telepon = '" +
                tambahkaryawan_noTelp.getText()+"', posisi_kerja = '"
                + tambahkaryawan_posisiKerja.getSelectionModel().getSelectedItem() + "', gambar = '"
                + url+"'  tanggal = '"+tanggalDatabase+"' WHERE id_karyawan = '"
                +tambahkaryawan_IDKaryawan.getText()+"'";

        connection = Database.connectionDatabase();

        try {
            Alert alert;
            if(tambahkaryawan_IDKaryawan.getText().isEmpty()
                    || tambahkaryawan_NamaLengkap.getText().isEmpty()
                    || tambahkaryawan_noTelp.getText().isEmpty()
                    || tambahkaryawan_jenisKelamin.getSelectionModel().getSelectedItem() == null
                    || tambahkaryawan_posisiKerja.getSelectionModel().getSelectedItem() == null
                    || DataFetch.path == null || DataFetch.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pesan error!");
                alert.setHeaderText(null);
                alert.setContentText("Tolong masukkan kolom yang kosong!");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Pesan Konfirmasi");
                alert.setHeaderText(null);
                alert.setContentText("Yakin untuk mengubah ID Karyawan? " +
                        tambahkaryawan_IDKaryawan.getText()+"?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connection.createStatement();
                    statement.executeUpdate(sql);

                    double gaji = 0;
                    String periksa = "SELECT * FROM informasi_karyawan WHERE id_karyawan = '"
                            + tambahkaryawan_IDKaryawan.getText() + "'";

                    prepare = connection.prepareStatement(periksa);
                    result = prepare.executeQuery();

                    while (result.next()) {
                        gaji = result.getDouble("gaji");
                    }

                    String update = "UPDATE informasi_karyawan SET nama_lengkap = '"
                            + tambahkaryawan_NamaLengkap.getText() + "', position = '"
                            + tambahkaryawan_posisiKerja.getSelectionModel().getSelectedItem()
                            + "' WHERE employee_id = '"
                            + tambahkaryawan_IDKaryawan.getText() + "'";
                    prepare = connection.prepareStatement(update);
                    prepare.executeUpdate();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Pesan Informasi");
                    alert.setHeaderText(null);
                    alert.setContentText("Data berhasil diperbarui!");
                    alert.showAndWait();

                    tampilkanDaftarKaryawanTambahan();
                    muatUlang();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Pencarian() {
        FilteredList<DataKaryawan> filter = new FilteredList<>(tambahDaftarKaryawan, e -> true);

        tambahkaryawan_pencarian.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predikatDataKaryawan -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String kunci = newValue.toLowerCase();

                if (predikatDataKaryawan.getIDKaryawan().toString().contains(kunci)) {
                    return true;
                }
                else if (predikatDataKaryawan.getNamaLengkap().toLowerCase().contains(kunci)) {
                    return true;
                }
                else if (predikatDataKaryawan.getNomorTelepon().toLowerCase().contains(kunci)) {
                    return true;
                }
                else if (predikatDataKaryawan.getJenisKelamin().toLowerCase().contains(kunci)) {
                    return true;
                }
                else if (predikatDataKaryawan.getPosisiKerja().toLowerCase().contains(kunci)) {
                    return true;
                }
                else if (predikatDataKaryawan.getTanggal().toString().contains(kunci)) {
                    return true;
                }
                else return false;
            });
        });

        SortedList<DataKaryawan> urutan = new SortedList<>(filter);
        urutan.comparatorProperty().bind(tambahkaryawan_tableView.comparatorProperty());
        tambahkaryawan_tableView.setItems(urutan);
    }
    public ObservableList<DataKaryawan> tambahDataKaryawan() {
        ObservableList<DataKaryawan> daftarData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM karyawan";

        try {
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();
            DataKaryawan pekerja;

            while (result.next()) {
                pekerja = new DataKaryawan(result.getInt("employee_id"),
                        result.getString("nama_lengkap"),
                        result.getString("jenis_kelamin"),
                        result.getString("nomor_telepon"),
                        result.getString("posisi_kerja"),
                        result.getString("gambar"),
                        result.getDate("tanggal")
                );
                daftarData.add(pekerja);
                daftarData.add(pekerja);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daftarData;
    }

    private ObservableList<DataKaryawan> tambahDaftarKaryawan;
    public void tampilkanDaftarKaryawanTambahan() {
        tambahDaftarKaryawan = tambahDataKaryawan();

        tambahkaryawan_kolom_IDKaryawan.setCellValueFactory(new PropertyValueFactory<>("IDKaryawan"));
        tambahkaryawan_kolom_NamaLengkap.setCellValueFactory(new PropertyValueFactory<>("NamaLengkap"));
        tambahkaryawan_kolom_NomorTelp.setCellValueFactory(new PropertyValueFactory<>("NomorTelp"));
        tambahkaryawan_kolom_JenisKelamin.setCellValueFactory(new PropertyValueFactory<>("JenisKelamin"));
        tambahkaryawan_kolom_PosisiKerja.setCellValueFactory(new PropertyValueFactory<>("PosisiKerja"));
        tambahkaryawan_kolom_TanggalMasuk.setCellValueFactory(new PropertyValueFactory<>("TanggalMasuk"));

        tambahkaryawan_tableView.setItems(tambahDaftarKaryawan);
    }

    public void tambahPilihanKaryawan() {
        DataKaryawan data = tambahkaryawan_tableView.getSelectionModel().getSelectedItem();
        int angka = tambahkaryawan_tableView.getSelectionModel().getSelectedIndex();

        if((angka -1) < -1) {return;}
        tambahkaryawan_IDKaryawan.setText(String.valueOf(data.getIDKaryawan()));
        tambahkaryawan_NamaLengkap.setText(String.valueOf(data.getNamaLengkap()));
        tambahkaryawan_noTelp.setText(String.valueOf(data.getNomorTelepon()));

        DataFetch.path = data.getGambar();

        String link = "file: " + data.getGambar();
        gambar = new Image(link, 113, 150, false, true);
        tambahkaryawan_gambar.setImage(gambar);
    }

    public void gajiUpdate() {
        String sql = "UPDATE info_karyawan SET gaji = '" + gaji_GajiKaryawan.getText()
                + "' WHERE id_karyawan = '" + gaji_IDKaryawan.getText() + "'";

        connection = Database.connectionDatabase();

        try {
            Alert alert;

            if (gaji_IDKaryawan.getText().isEmpty()
                    || gaji_namalengkap.getText().isEmpty()
                    || gaji_posisikerja.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pesan Salah!");
                alert.setHeaderText(null);
                alert.setContentText("Pilih item dahulu!");
                alert.showAndWait();
            } else {
                statement = connection.createStatement();
                statement.executeUpdate(sql);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pesan Informasi");
                alert.setHeaderText(null);
                alert.setContentText("Data berhasil diperbarui!");
                alert.showAndWait();

                tampilkanDaftarGaji();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void muatulangGaji() {
        gaji_IDKaryawan.setText("");
        gaji_namalengkap.setText("");
        gaji_posisikerja.setText("");
        gaji_GajiKaryawan.setText("");
    }
    public ObservableList<DataKaryawan> daftarGaji() {
        ObservableList<DataKaryawan> daftarData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM informasi_karyawan";

        connection = Database.connectionDatabase();

        try {
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();

            DataKaryawan data;

            while (result.next()) {
                data = new DataKaryawan(result.getInt("id_karyawan")
                        , result.getString("nama_lengkap")
                        , result.getString("posisi_kerja")
                        , result.getDouble("gaji"));

                daftarData.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } return daftarData;
    }

    private ObservableList<DataKaryawan> dataGaji;
    public void tampilkanDaftarGaji() {
        dataGaji = daftarGaji();
        gaji_kolom_IDKaryawan.setCellValueFactory(new PropertyValueFactory<>("id_karyawan"));
        gaji_kolom_NamaLengkap.setCellValueFactory(new PropertyValueFactory<>("nama_lengkap"));
        gaji_kolom_PosisiKerja.setCellValueFactory(new PropertyValueFactory<>("posisi_kerja"));
        gaji_kolom_GajiKaryawan.setCellValueFactory(new PropertyValueFactory<>("gaji"));

        gaji_tableView.setItems(dataGaji);
    }

    public void pilihGaji() {
        DataKaryawan data = gaji_tableView.getSelectionModel().getSelectedItem();
        int angka = gaji_tableView.getSelectionModel().getSelectedIndex();

        if((angka -1 ) < -1 ) {return;}

        gaji_IDKaryawan.setText(String.valueOf(data.getIDKaryawan()));
        gaji_namalengkap.setText(data.getNamaLengkap());
        gaji_posisikerja.setText(data.getPosisiKerja());
        gaji_GajiKaryawan.setText(String.valueOf(data.getGaji()));
    }

    public void navbarOriginal() {
        beranda_button.setStyle("-fx-background-color: linear-gradient(to bottom right, #579653, #2add1d)");
    }
    public void tampilkanUsername() {
        username.setText(DataFetch.username);
    }

    public void tukarForm(ActionEvent event) {
        if (event.getSource() == beranda_button) {
            beranda_form.setVisible(true);
            tambahkaryawan_form.setVisible(false);
            gaji_form.setVisible(false);

            beranda_button.setStyle("-fx-background-color: linear-gradient(to bottom right, #579653, #2add1d)");
            tambahkaryawan_button.setStyle("-fx-background-color:transparent");
            gaji_button.setStyle("-fx-background-color:transparent");

            totalKaryawanHome();
            totalKehadiranHome();
            karyawanNonAktifHome();
            Diagram();
        }
        else if (event.getSource() == tambahkaryawan_button) {
            tambahkaryawan_form.setVisible(true);
            beranda_form.setVisible(false);
            gaji_form.setVisible(false);

            tambahkaryawan_button.setStyle("-fx-background-color: linear-gradient(to bottom right, #579653, #2add1d)");
            beranda_button.setStyle("-fx-background-color: transparent");
            gaji_button.setStyle("-fx-background-color: transparent");

            tambahJenisKelamin();
            tambahPosisiKaryawan();
            Pencarian();
        }
        else if (event.getSource() == gaji_button) {
            gaji_form.setVisible(true);
            beranda_form.setVisible(false);
            tambahkaryawan_form.setVisible(false);

            gaji_button.setStyle("-fx-background-color: linear-gradient(to bottom right, #579653, #2add1d)");
            beranda_button.setStyle("-fx-background-color:transparent");
            tambahkaryawan_button.setStyle("-fx-background-color:transparent");

            tampilkanDaftarGaji();
        }
    }

    private double x = 0;
    private double y = 0;

    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pesan Konfirmasi");
        alert.setHeaderText(null);
        alert.setContentText("Yakin ingin keluar? ");
        Optional<ButtonType> option = alert.showAndWait();

        try {
            if (option.get().equals(ButtonType.OK)) {

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);


                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tampilkanUsername();
        navbarOriginal();
        totalKaryawanHome();
        totalKehadiranHome();
        karyawanNonAktifHome();
        Diagram();

        tampilkanDaftarKaryawanTambahan();
        tambahJenisKelamin();
        tambahPosisiKaryawan();

        tampilkanDaftarGaji();
    }
}
