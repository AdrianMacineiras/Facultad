-module(gestor).

-export([start/1, stop/1]). % Api
-export([init/1]).
-export([avail/1]).
-export([alloc/1]).
-export([release/2]).

%%% API %%%%

start(Recursos) ->
	spawn(?MODULE, init, [Recursos]).

stop(Hash) ->
	Hash ! stop.

avail(Hash) -> 
    Hash ! {avail,self()},
    receive
        RecursosDisponibles ->
            RecursosDisponibles
    end.

alloc(Hash) ->
	Hash ! {alloc, self()},
	receive
		{alloc_ok, X}->{ok, X};
		{alloc_error, X} -> {error, X}
	end.

release(Hash,Elem) ->
	Hash ! {release, Elem, self()},
		receive
			{release_ok, X}->X;
			{release_error, X} -> {error, X}
		end.

%%% Internal functions %%%%%
init(Recursos) ->
	Almacen = [{self(),R} || R <- Recursos], 
	loop(Almacen).

loop(Almacen) -> 
	receive
		{avail, From}->
			Recursos_disponibles = [{P,R} || {P,R} <- Almacen , P == self()],
			From ! length(Recursos_disponibles),
			loop(Almacen);
		{alloc, From} ->
			Disponibles = [{P,R} || {P,R} <- Almacen , P == self()],
				case Disponibles of
				 	[] -> 
				 		From ! {alloc_error},
						loop(Almacen);
					_  ->	
						Asignados = [{P,R} || {P,R} <- Almacen , P /= self()],
						[Sale | Resto] = Disponibles,
						{_,R} = Sale,
						From ! {alloc_ok,R},
						loop(Resto ++ Asignados ++ [{From,R}])
				 end; 

		{release, Elem, From} -> 
			ALiberar = [{P,R} || {P,R} <- Almacen, P==From, R==Elem],
				case ALiberar of
					[] -> % Si no encuentra el elemento
						From ! {release_error, recurso_no_reservado},
						loop(Almacen);
					_ -> % Si encuentra el elemento
						[{P,R}] = ALiberar,
						A1 = lists:delete({P,R},Almacen), % Libera el elemento
						A2 = [ {self(),R} | A1 ], % Lo aÃ±ade al almacen
						From ! {release_ok, ok},
						loop(A2)			
				end;
		stop ->
			ok
	end.
