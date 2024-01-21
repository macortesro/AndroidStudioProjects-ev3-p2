package cl.mcortesr.android.ev3_p2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.mcortesr.android.ev3_p2.ui.ListaSolicitanteViewModel
import cl.mcortesr.android.ev3_p2.ui.theme.Ev3p2Theme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import cl.mcortesr.android.ev3_p2.data.Solicitante
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate
import androidx.lifecycle.lifecycleScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            appBancoUI()
        }
    }
}

@Composable
fun appBancoUI(
    navController: NavController = rememberNavController(),
    vmListaSolicitante: ListaSolicitanteViewModel = viewModel(factory = ListaSolicitanteViewModel.Factory)
){

    NavHost(
        navController = navController as NavHostController,
        startDestination = "Inicio" )
    {
        composable("inicio"){
            FormIngresoUI(navController)
        }
        composable("solicitarCuenta") {
            SolicitarCuenta()
        }
    }

}

@Composable
fun FormIngresoUI(navController: NavController) {
    val (usuario, setUsuario) = remember { mutableStateOf("") }
    val (contrasena, setContrasena) = remember { mutableStateOf("") }

    Column (
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(R.drawable.logo_iplabank),
            contentDescription = "Banco - Iplabank",
            modifier = Modifier.scale(1.8f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Ingreso al sistema",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = usuario,
            onValueChange = {setUsuario(it)},
            label = {Text("Usuario")}
        )
        TextField(
            value = contrasena,
            onValueChange = {setContrasena(it)},
            label = {Text("Contraseña")}
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { }) { Text("Ingresar")}
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            navController.navigate("solicitarCuenta")
        }) {
            Text("Solicitar Cuenta")
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun SolicitarCuenta(
    vmListaSolicitante: ListaSolicitanteViewModel = viewModel(factory = ListaSolicitanteViewModel.Factory)
){

    val contexto = LocalContext.current
    var mensaje by rememberSaveable { mutableStateOf("Ubicacion: ") }

    var nombre by rememberSaveable { mutableStateOf("") }
    var rut by rememberSaveable {  mutableStateOf("") }
    var fecnac by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("")}

    val lanzadorPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            if(
                it.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false)
                ||
                it.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false)

            ) {
                val locationServices = LocationServices.getFusedLocationProviderClient(contexto)
                val repository = UbicacionRepository(locationServices)
                repository.conseguirUbicacion(
                    onExito = {
                        mensaje = "Lat: ${it.latitude} | Long: ${it.longitude}"
                    },
                    onError = {
                        mensaje = "No se pudo conseguir la ubicación."
                        Log.e("AppUbicacionUI::conseguirUbicacion", it.message.toString())
                    }
                )
            } else {
                // Permisos NO concedidos
                // por que se necesitan los permisos
                mensaje = "Debe otorgar permisos para que la aplicacion pueda funcionar."
            }
        }
    )



    Column (
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Image(
            painter = painterResource(R.drawable.logo_iplabank),
            contentDescription = "Banco - Iplabank",
            modifier = Modifier.scale(1.8f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Solicitud Cuenta",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = nombre,
            onValueChange = {nombre = it},
            label = {Text("Nombre Completo")}
        )
        TextField(
            value = rut,
            onValueChange = {rut = it},
            label = {Text("RUT")}
        )
        TextField(
            value = fecnac,
            onValueChange = {fecnac = it},
            label = {Text("Fecha de Nacimiento")}
        )
        TextField(
            value = email,
            onValueChange = { email = it},
            label = {Text("Email")}
        )
        TextField(
            value = telefono,
            onValueChange = {telefono = it},
            label = {Text("Telefono")}
        )

        Spacer(modifier = Modifier.height(10.dp))

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            Log.v("Error evento click", "Error Ubicacion")
            lanzadorPermisos.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }) {
            Text("Ubicacion")

        }
        vmListaSolicitante.ubicacion?.let {
            Text("Lat : ${it.latitude} | Long: ${it.longitude}")
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {}
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
               Text("Cédula Frontal")
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {}
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text("Cédula Trasera")
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {

            vmListaSolicitante.insertarSolicitante(
                Solicitante(
                    null,
                    nombre,
                    rut,
                    LocalDate.parse(fecnac),
                    email,
                    telefono,
                    vmListaSolicitante.ubicacion?.latitude ?: 0.0,
                    vmListaSolicitante.ubicacion?.longitude ?: 0.0,
                    "",
                    "",
                    LocalDate.now()
                )
            )
        }){
            Text("SOLICITAR")
        }


    }

}






